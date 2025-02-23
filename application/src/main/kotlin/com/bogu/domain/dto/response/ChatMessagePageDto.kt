package com.bogu.domain.dto.response

import com.bogu.domain.dto.ChatMessageDto

data class ChatMessagePageDto(
    val roomId: Long,
    val limit: Int,
    val offset: Int,
    val hasNext: Boolean,
    val chatMessages: List<ChatMessageDto>
)