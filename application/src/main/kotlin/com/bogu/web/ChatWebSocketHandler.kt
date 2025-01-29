package com.bogu.web

import com.bogu.domain.model.ChatMessage
import com.bogu.service.ChatRoomService
import com.bogu.domain.model.ChatMessageType
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
    private val chatRoomService: ChatRoomService,
) : TextWebSocketHandler() {

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info { "New session connected: ${session.id}" }
        // 연결된 시점에는 아직 어느 방에도 들어가지 않은 상태
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.info { "Message received from ${session.id}: ${message.payload}" }

        try {
            val chatMessage: ChatMessage = objectMapper.readValue(message.payload, ChatMessage::class.java)

            val roomId = chatMessage.roomId

            when (chatMessage.type) {
                ChatMessageType.JOIN -> {
                    // 해당 roomId가 없으면 생성

                    chatRoomService.join(roomId, session)
                    // 세션 입장 처리


                    // System 알림 메시지 예시
                    val notice = ChatMessage(
                        type = ChatMessageType.CHAT,
                        roomId = roomId,
                        sender = "System",
                        content = "${chatMessage.sender} joined the room."
                    )
                    val noticeText = objectMapper.writeValueAsString(notice)
                    chatRoomService.broadcast(roomId, TextMessage(noticeText))
                }

                ChatMessageType.CHAT -> {
                    // 채팅 메시지

                    // 실제 채팅 메시지 DB 저장 로직(선택)
                    // messageRepository.save(...)

                    // 해당 Room 세션에게만 메시지 브로드캐스트
                    chatRoomService.broadcast(roomId, TextMessage(message.payload))
                }

                ChatMessageType.LEAVE -> {
                    chatRoomService.leave(roomId, session)

                    val notice = ChatMessage(
                        type = ChatMessageType.CHAT,
                        roomId = roomId,
                        sender = "System",
                        content = "${chatMessage.sender} left the room."
                    )
                    chatRoomService.broadcast(roomId, TextMessage(objectMapper.writeValueAsString(notice)))
                }
            }
        } catch (e: Exception) {
            logger.error(e) { "Error parsing or processing message" }
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.info { "Session closed: ${session.id}" }

        // 세션이 끊겼으므로, 해당 Room에서도 제거
        chatRoomService.leave(session)
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        logger.error { "Error in session ${session.id}: ${exception.message}" }
    }

    companion object : KLogging()
}