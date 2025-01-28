package com.bogu.service

import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Service
class ChatRoomService(
    private val sessionCacheService: SessionCacheService
) {
    fun join(roomId: Long, session: WebSocketSession) {
        sessionCacheService.add(roomId, session)
    }

    fun leave(session: WebSocketSession) {
        sessionCacheService.remove(session)
    }

    fun leave(roomId: Long, session: WebSocketSession) {
        sessionCacheService.remove(roomId, session)
    }

    fun broadcast(roomId: Long, message: TextMessage) {
        sessionCacheService.getSessions(roomId)?.forEach { session ->
            session.sendMessage(message)
        }
    }
}