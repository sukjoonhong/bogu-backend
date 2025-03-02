package com.bogu.web.controller

import com.bogu.domain.dto.request.LoginRequest
import com.bogu.domain.dto.response.LoginResponse
import com.bogu.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.nio.file.Files
import java.nio.file.Paths

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {
    @GetMapping("/public-key")
    fun getPublicKey(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("publicKey" to authService.getPublicKey()))
    }

    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest,
    ): LoginResponse {
        return authService.login(loginRequest)
    }
}