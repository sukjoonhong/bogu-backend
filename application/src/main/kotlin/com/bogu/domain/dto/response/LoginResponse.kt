package com.bogu.domain.dto.response

import com.bogu.domain.dto.MemberDto


data class LoginResponse(
    val id: Long,
    val token: String,
    val careGiverCandidates: List<MemberDto>
)
