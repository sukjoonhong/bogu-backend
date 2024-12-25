package com.bogu.bogubackend

import mu.KLogging
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class ChatWebSocketHandler : TextWebSocketHandler() {

    private val sessions = mutableSetOf<WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions.add(session)
        logger.info { "New session connected: ${session.id}" }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.info { "Message received: ${message.payload}" }

        // 모든 연결된 클라이언트에게 메시지 전송
        sessions.forEach { it.sendMessage(message) }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session)
        logger.info { "Session closed: ${session.id}" }
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        logger.error { "Error in session ${session.id}: ${exception.message}" }
    }

    companion object : KLogging()
}