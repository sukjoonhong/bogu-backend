package com.bogu.domain.model


data class ChatMessage(
    val type: ChatMessageType,
    val roomId: Long,
    val sender: String,
    val content: String
)

enum class ChatMessageType {
    JOIN, CHAT, LEAVE
}