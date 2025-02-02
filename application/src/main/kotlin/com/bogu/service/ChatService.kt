package com.bogu.service

import com.bogu.domain.ChatMessageType
import com.bogu.domain.dto.ChatMessageDto
import com.bogu.service.crud.ChatRoomCrudService
import com.bogu.util.RoomId
import mu.KLogging
import org.jetbrains.annotations.TestOnly
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession

@Service
class ChatService(
    private val chatRoomSessionService: ChatRoomSessionService,
    private val chatRoomCrudService: ChatRoomCrudService
) {
    fun handleMessage(session: WebSocketSession, chatMessageDto: ChatMessageDto) {
        try {
            when (chatMessageDto.type) {
                ChatMessageType.JOIN -> {
                    join(session, chatMessageDto)
                }

                ChatMessageType.CHAT -> {
                    chatToAll(chatMessageDto)
                }

                ChatMessageType.LEAVE -> {
                    leave(session, chatMessageDto)
                }
            }
        } catch (e: Exception) {
            logger.error(e) { "Error parsing or processing message" }
        }
    }

    private fun chatToAll(chatMessageDto: ChatMessageDto) {
        chatRoomSessionService.broadcast(genEchoMessage(chatMessageDto))
    }

    private fun join(session: WebSocketSession, chatMessageDto: ChatMessageDto) {
        chatRoomSessionService.save(chatMessageDto.roomId, session)
    }

    private fun leave(session: WebSocketSession, chatMessageDto: ChatMessageDto) {
        chatRoomSessionService.broadcast(genLeaveMessage(chatMessageDto))
        chatRoomCrudService.softDeleteBy(chatMessageDto.senderId, chatMessageDto.receiverId)
        chatRoomSessionService.delete(session)
    }

    fun createPairedChatRooms(senderId: Long, receiverId: Long): RoomId {
        chatRoomCrudService.createPairedChatRooms(senderId, receiverId)
        val roomId = chatRoomCrudService.findRoomIdBy(senderId, receiverId)
        requireNotNull(roomId) { "no room with id $senderId exists" }
        return roomId
    }

    @TestOnly
    private fun genEchoMessage(chatMessageDto: ChatMessageDto): ChatMessageDto {
        return ChatMessageDto(
            type = ChatMessageType.CHAT,
            roomId = chatMessageDto.roomId,
            senderId = chatMessageDto.receiverId,
            receiverId = chatMessageDto.senderId,
            content = chatMessageDto.content,
        )
    }

    @TestOnly
    private fun genLeaveMessage(chatMessageDto: ChatMessageDto): ChatMessageDto {
        return ChatMessageDto(
            type = ChatMessageType.LEAVE,
            roomId = chatMessageDto.roomId,
            senderId = chatMessageDto.senderId,
            receiverId = chatMessageDto.receiverId,
            content = "${chatMessageDto.senderId} left the room."
        )
    }

    companion object : KLogging()
}