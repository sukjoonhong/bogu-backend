package com.bogu.domain.dto


data class ChatRoomDto(
    val id: Long,
    val lastMessage: String,
    val lastMessageSentAt: String,
    val members: List<MemberDto>,
    val chatMessages: List<ChatMessageDto>,
    val offset: Int,
    val limit: Int,
    val hasNext: Boolean
)