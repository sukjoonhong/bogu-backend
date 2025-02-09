package com.bogu.service

import com.bogu.domain.dto.ChatMessageDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Service
class ChatRoomSessionService(
    private val objectMapper: ObjectMapper,
    private val sessionCacheService: SessionCacheService
) {
    fun save(roomId: Long, senderId: Long, session: WebSocketSession) {
        sessionCacheService.add(roomId, senderId, session)
    }

    fun delete(session: WebSocketSession) {
        sessionCacheService.remove(session)
    }

    fun broadcast(message: ChatMessageDto) {
        sessionCacheService.getSessions(message.roomId)?.forEach { session ->
            if (sessionCacheService.getMemberSession(session.id) == message.senderId) {
                return@forEach
            }
            session.sendMessage(message.toTextMessage())
        }
    }

    private fun ChatMessageDto.toTextMessage(): TextMessage {
        return TextMessage(objectMapper.writeValueAsString(this))
    }
}