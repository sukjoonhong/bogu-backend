package com.bogu.domain.dto

data class MemberDto(
    val id: Long,
    val name: String,
    val profile: ProfileDto?,
    val nickName: String,
)
