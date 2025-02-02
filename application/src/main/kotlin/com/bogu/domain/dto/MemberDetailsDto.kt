package com.bogu.domain.dto

data class MemberDetailsDto (
    val id: Long,
    val careGiverCandidates: List<MemberDto>,
    val chatRooms: List<ChatRoomDto>,
)