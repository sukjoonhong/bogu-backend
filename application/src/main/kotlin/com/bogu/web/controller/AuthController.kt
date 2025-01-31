package com.bogu.web.controller

import com.bogu.domain.dto.request.LoginRequest
import com.bogu.domain.dto.response.LoginResponse
import com.bogu.service.AuthService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {

    @GetMapping("/login")
    fun login(
        @RequestParam(value = "id", required = true) id: Long,
        @RequestParam(value = "password", required = true) password: String,
    ): LoginResponse {
        return authService.login(LoginRequest(id, password))
    }
}