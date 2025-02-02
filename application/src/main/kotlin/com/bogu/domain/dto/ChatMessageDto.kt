package com.bogu.domain.dto

import com.bogu.domain.ChatMessageType
import java.time.LocalDateTime


data class ChatMessageDto(
    val type: ChatMessageType,
    val roomId: Long,
    val senderId: Long,
    val receiverId: Long,
    val content: String,
    val createdAt: LocalDateTime? = null
)