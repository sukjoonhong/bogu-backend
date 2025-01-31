package com.bogu.service

import com.bogu.domain.dto.ChatMessage
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Service
class ChatRoomSessionService(
    private val objectMapper: ObjectMapper,
    private val sessionCacheService: SessionCacheService
) {
    fun save(roomId: Long, session: WebSocketSession) {
        sessionCacheService.add(roomId, session)
    }

    fun delete(session: WebSocketSession) {
        sessionCacheService.remove(session)
    }

    fun broadcast(message: ChatMessage) {
        sessionCacheService.getSessions(message.roomId)?.forEach { session ->
            session.sendMessage(message.toTextMessage())
        }
    }

    private fun ChatMessage.toTextMessage(): TextMessage {
        return TextMessage(objectMapper.writeValueAsString(this))
    }
}