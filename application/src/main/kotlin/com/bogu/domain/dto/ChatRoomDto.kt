package com.bogu.domain.dto

data class ChatRoomDto(
    val id: Long,
    val receiverName: String,
    val lastMessage: String,
    val masterId: Long,
    val slaveId: Long,
    val chatMessages: List<ChatMessageDto>
)