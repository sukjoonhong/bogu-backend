package com.bogu.domain.dto.response

import com.bogu.domain.dto.MemberDetailsDto


data class LoginResponse(
    val token: String,
    val refreshToken: String?,
    val memberDetailsDto: MemberDetailsDto
)
