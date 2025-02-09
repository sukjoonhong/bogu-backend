package com.bogu.domain.dto

import com.bogu.domain.ChatMessageType


data class ChatMessageDto(
    val type: ChatMessageType,
    val roomId: Long,
    val senderId: Long,
    val content: String,
    val createdAt: String? = null
)