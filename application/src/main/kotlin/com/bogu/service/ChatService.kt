package com.bogu.service

import com.bogu.domain.dto.ChatMessage
import com.bogu.domain.dto.ChatMessageType
import com.bogu.service.crud.RoomCrudService
import com.bogu.util.RoomId
import com.bogu.web.ChatWebSocketHandler.Companion.logger
import org.jetbrains.annotations.TestOnly
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession

@Service
class ChatService(
    private val chatRoomSessionService: ChatRoomSessionService,
    private val roomCrudService: RoomCrudService
) {
    fun handleMessage(session: WebSocketSession, chatMessage: ChatMessage) {
        try {
            when (chatMessage.type) {
                ChatMessageType.JOIN -> {
                    join(session, chatMessage)
                }

                ChatMessageType.CHAT -> {
                    chat(chatMessage)
                }

                ChatMessageType.LEAVE -> {
                    leave(session, chatMessage)
                }
            }
        } catch (e: Exception) {
            logger.error(e) { "Error parsing or processing message" }
        }
    }

    private fun chat(chatMessage: ChatMessage) {
        chatRoomSessionService.broadcast(genEchoMessage(chatMessage))
    }

    private fun join(session: WebSocketSession, chatMessage: ChatMessage) {
        val roomId = roomCrudService.getOrCreate(chatMessage)
        chatRoomSessionService.save(roomId, session)
    }

    private fun leave(session: WebSocketSession, chatMessage: ChatMessage) {
        chatRoomSessionService.broadcast(genLeaveMessage(chatMessage))
        roomCrudService.deleteBy(chatMessage.sender, chatMessage.receiver)
        chatRoomSessionService.delete(session)
    }

    fun getOrCreateRoom(leftWingId: Long, rightWingId: Long): RoomId {
        return roomCrudService.getOrCreate(leftWingId, rightWingId)
    }

    @TestOnly
    private fun genEchoMessage(chatMessage: ChatMessage): ChatMessage {
        return ChatMessage(
            type = ChatMessageType.CHAT,
            roomId = chatMessage.roomId,
            sender = chatMessage.receiver,
            receiver = chatMessage.sender,
            content = chatMessage.content,
        )
    }

    @TestOnly
    private fun genLeaveMessage(chatMessage: ChatMessage): ChatMessage {
        return ChatMessage(
            type = ChatMessageType.LEAVE,
            roomId = chatMessage.roomId,
            sender = chatMessage.sender,
            receiver = chatMessage.receiver,
            content = "${chatMessage.sender} left the room."
        )
    }
}