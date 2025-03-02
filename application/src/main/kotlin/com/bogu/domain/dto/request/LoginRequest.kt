package com.bogu.domain.dto.request

data class LoginRequest(
    val authId: String,
    val password: String,
)
