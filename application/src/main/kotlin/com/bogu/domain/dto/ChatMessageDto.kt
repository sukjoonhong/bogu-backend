package com.bogu.domain.dto

import com.bogu.domain.ChatMessageType


data class ChatMessageDto(
    val type: ChatMessageType,
    val roomId: Long,
    val senderId: Long,
    val content: String,
    val createdAt: String
) {
    companion object {
        const val FETCH_SIZE = 50
        const val DEFAULT_FETCH_SIZE = 3
    }
}