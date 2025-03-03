package com.bogu.web

import com.bogu.domain.dto.ChatMessageDto
import com.bogu.security.JwtUtil
import com.bogu.service.chat.ChatRoomSessionService
import com.bogu.service.chat.ChatService
import com.bogu.service.crud.MemberCrudService
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
    private val memberCrudService: MemberCrudService
) : TextWebSocketHandler() {

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val token = extractToken(session) ?: run {
            session.close(CloseStatus.NOT_ACCEPTABLE)
            return
        }

        // 토큰 검증
        if (!JwtUtil.validateToken(token)) {
            session.close(CloseStatus.NOT_ACCEPTABLE)
            return
        }

        // 토큰에서 authId(=memberId) 추출
        val memberId = JwtUtil.extractAuthId(token) ?: return
        val member = memberCrudService.findByAuthId(memberId)

        logger.info { "WebSocket connected: sessionId=${session.id}, memberId=$memberId" }

        // 전역 세션(글로벌) 등록
        chatRoomSessionService.add(session, member.id)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.info { "Message received from sessionId=${session.id}: ${message.payload}" }

        // JSON -> ChatMessageDto 변환
        val chatMessageDto = toChatMessage(message)

        chatService.handleMessage(session, chatMessageDto)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.info { "Session closed: sessionId=${session.id}, status=$status" }
        chatRoomSessionService.remove(session)
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        logger.error { "Transport error in sessionId=${session.id}: ${exception.message}" }
    }

    private fun toChatMessage(message: TextMessage): ChatMessageDto {
        return objectMapper.readValue(message.payload, ChatMessageDto::class.java)
    }

    private fun extractToken(session: WebSocketSession): String? {
        val queryParams = session.uri?.query ?: return null
        return queryParams.split("&")
            .firstOrNull { it.startsWith("token=") }
            ?.substringAfter("token=")
    }

    companion object : KLogging()
}
