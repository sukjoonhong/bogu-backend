package com.bogu.domain.dto

data class ChatRoomDto(
    val id: Int,
    val receiverName: String,
    val lastMessage: String,
    val receiverId: Long,
)