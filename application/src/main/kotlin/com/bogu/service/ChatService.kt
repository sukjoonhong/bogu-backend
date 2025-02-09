package com.bogu.service

import com.bogu.domain.ChatMessageType
import com.bogu.domain.dto.ChatMessageDto
import com.bogu.service.crud.ChatMessageCrudService
import com.bogu.service.crud.ChatRoomCrudService
import com.bogu.service.crud.ChatRoomMemberCrudService
import com.bogu.util.RoomId
import mu.KLogging
import org.jetbrains.annotations.TestOnly
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession

@Service
class ChatService(
    private val chatRoomSessionService: ChatRoomSessionService,
    private val chatRoomMemberCrudService: ChatRoomMemberCrudService,
    private val chatRoomCrudService: ChatRoomCrudService,
    private val chatMessageCrudService: ChatMessageCrudService,
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
        chatRoomSessionService.broadcast(chatMessageDto)
        chatMessageCrudService.save(chatMessageDto)
    }

    private fun join(session: WebSocketSession, chatMessageDto: ChatMessageDto) {
        chatRoomSessionService.save(chatMessageDto.roomId, chatMessageDto.senderId, session)
    }

    private fun leave(session: WebSocketSession, chatMessageDto: ChatMessageDto) {
        chatRoomSessionService.broadcast(genLeaveMessage(chatMessageDto))
        chatRoomSessionService.delete(session)
    }

    fun createDirectChatRoom(initiatorId: Long, respondentId: Long): RoomId {
        val chatRoomId = chatRoomCrudService.createOrGetDirectChatRoom(initiatorId, respondentId)
        chatRoomMemberCrudService.createChatRoomMembers(chatRoomId, listOf(initiatorId, respondentId))
        return chatRoomId
    }

    @TestOnly
    private fun genEchoMessage(chatMessageDto: ChatMessageDto): ChatMessageDto {
        return ChatMessageDto(
            type = ChatMessageType.CHAT,
            roomId = chatMessageDto.roomId,
            senderId = 123, //TODO: 상대 멤버 아이디
            content = chatMessageDto.content,
        )
    }

    @TestOnly
    private fun genLeaveMessage(chatMessageDto: ChatMessageDto): ChatMessageDto {
        return ChatMessageDto(
            type = ChatMessageType.LEAVE,
            roomId = chatMessageDto.roomId,
            senderId = chatMessageDto.senderId,
            content = "${chatMessageDto.senderId} left the room."
        )
    }

    companion object : KLogging()
}