package com.bogu.service.chat

import com.bogu.domain.ChatMessageType
import com.bogu.domain.dto.ChatMessageDto
import com.bogu.service.PushNotificationService
import com.bogu.service.crud.ChatMessageCrudService
import com.bogu.service.crud.ChatRoomCrudService
import com.bogu.service.crud.ChatRoomMemberCrudService
import mu.KLogging
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession


@Service
class ChatService(
    private val chatRoomSessionService: ChatRoomSessionService,
    private val chatRoomMemberCrudService: ChatRoomMemberCrudService,
    private val chatRoomCrudService: ChatRoomCrudService,
    private val chatMessageCrudService: ChatMessageCrudService,
    private val pushNotificationService: PushNotificationService
) {
    fun handleMessage(session: WebSocketSession, chatMessageDto: ChatMessageDto) {
        try {
            when (chatMessageDto.type) {
                ChatMessageType.JOIN -> {
                    logger.info { "JOIN: ${chatMessageDto.senderId} joined roomId=${chatMessageDto.roomId}" }
                    join(chatMessageDto)
                }

                ChatMessageType.CHAT -> {
                    // 모든 사용자에게 메시지 전송 (글로벌)
                    sendChatMessage(chatMessageDto)
                }

                ChatMessageType.LEAVE -> {
                    // LEAVE도 방이 없으니 굳이 처리할 필요가 없을 수도 있음
                    logger.info { "LEAVE: ${chatMessageDto.senderId} left roomId=${chatMessageDto.roomId}" }
                    // 필요하다면 메시지 형태로 알림
                }

                ChatMessageType.ROOM_CREATE -> {
                    logger.info { "ROOM_CREATE: ${chatMessageDto.senderId}" }
                    // 필요 시 방 생성 로직 (DB)
                }

                ChatMessageType.ROOM_UPDATE -> {
                    logger.info { "ROOM_UPDATE: ${chatMessageDto.senderId}" }
                    // 필요 시 방 정보 갱신 로직 (DB)
                }
            }
        } catch (e: Exception) {
            logger.error(e) { "Error parsing or processing message" }
        }
    }

    private fun join(chatMessageDto: ChatMessageDto) {
        chatRoomSessionService.addMemberToRoom(chatMessageDto.roomId, chatMessageDto.senderId)
    }

    private fun sendChatMessage(chatMessageDto: ChatMessageDto) {
        val memberIds = chatRoomSessionService.getMemberIdsInRoom(chatMessageDto.roomId)

        try {
            chatMessageCrudService.save(chatMessageDto)

            memberIds.forEach { memberId ->
                // 자신 제외 로직
                if (memberId == chatMessageDto.senderId) return@forEach

                val session = chatRoomSessionService.getSessionBy(memberId)
                if (session == null || !session.isOpen) {
                    // 세션이 없거나 닫혀 있음 -> 푸시 알림
                    pushNotificationService.sendPushNotification(memberId, chatMessageDto)
                } else {
                    // 열려 있음 -> WebSocket 메시지 전송
                    session.sendMessage(chatRoomSessionService.toTextMessage(chatMessageDto))
                }
            }
        } catch (e: Exception) {
            logger.error(e) { "error sending chat message" }
        }
    }

    // 필요 시 별도 메서드
    private fun genLeaveMessage(chatMessageDto: ChatMessageDto): ChatMessageDto {
        return ChatMessageDto(
            type = ChatMessageType.LEAVE,
            roomId = chatMessageDto.roomId,
            senderId = chatMessageDto.senderId,
            content = "${chatMessageDto.senderId} left the room.",
            createdAt = chatMessageDto.createdAt,
        )
    }

    fun createOrUpdateDirectChatRoom(initiatorId: Long, respondentId: Long): Long {
        val chatRoomId = chatRoomCrudService.createOrUpdateDirectChatRoom(initiatorId, respondentId)
        chatRoomMemberCrudService.createChatRoomMembers(chatRoomId, listOf(initiatorId, respondentId))
        return chatRoomId
    }

    companion object : KLogging()
}