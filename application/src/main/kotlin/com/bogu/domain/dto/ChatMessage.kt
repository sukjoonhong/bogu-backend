package com.bogu.domain.dto

import java.time.LocalDateTime


data class ChatMessage(
    val type: ChatMessageType,
    val roomId: Long,
    val sender: Long,
    val receiver: Long,
    val content: String,
    val createdAt: LocalDateTime? = null
)

enum class ChatMessageType {
    JOIN, CHAT, LEAVE
}