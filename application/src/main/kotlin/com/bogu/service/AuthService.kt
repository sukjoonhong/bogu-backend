package com.bogu.service

import com.bogu.domain.dto.request.LoginRequest
import com.bogu.domain.dto.response.LoginResponse
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberService: MemberService
) {
    fun login(loginRequest: LoginRequest): LoginResponse {
        logger.info { "login request: $loginRequest" }
        val memberDetailsDto = memberService.loadInitialMemberInfos(TEST_USER_AUTH_ID)
        val loginResponse = LoginResponse(
            token = "testToken",
            memberDetailsDto = memberDetailsDto
        )
        logger.info { "login response: $loginResponse" }
        return loginResponse
    }

    companion object : KLogging() {
        const val TEST_USER_AUTH_ID = "dummyId1"
    }
}