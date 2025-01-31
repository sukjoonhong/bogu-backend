package com.bogu.web

import com.bogu.domain.dto.ChatMessage
import com.bogu.service.ChatRoomSessionService
import com.bogu.service.ChatService
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogging
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class ChatWebSocketHandler(
    private val objectMapper: ObjectMapper,
    private val chatService: ChatService,
    private val chatRoomSessionService: ChatRoomSessionService,
) : TextWebSocketHandler() {

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info { "new session connected: ${session.id}" }
        // 연결된 시점에는 아직 어느 방에도 들어가지 않은 상태
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.info { "message received from ${session.id}: ${message.payload}" }
        chatService.handleMessage(session = session, chatMessage = message.toChatMessage())
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.info { "session closed: ${session.id}" }
        chatRoomSessionService.delete(session)
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        logger.error { "error in session ${session.id}: ${exception.message}" }
    }

    private fun TextMessage.toChatMessage(): ChatMessage {
        return objectMapper.readValue(this.payload, ChatMessage::class.java)
    }

    companion object : KLogging()
}