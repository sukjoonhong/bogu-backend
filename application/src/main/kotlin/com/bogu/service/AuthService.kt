package com.bogu.service

import com.bogu.domain.dto.request.LoginRequest
import com.bogu.domain.dto.response.LoginResponse
import com.bogu.security.JwtUtil
import com.bogu.security.RsaUtil
import com.bogu.service.crud.MemberCrudService
import mu.KLogging
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberService: MemberService,
    private val memberCrudService: MemberCrudService,
    private val rsaUtil: RsaUtil,
) {
    fun getPublicKey(): String {
        return rsaUtil.getPublicKeyAsString()
    }

    fun login(loginRequest: LoginRequest): LoginResponse {
        logger.info { "login request: $loginRequest" }

        val decryptedPassword = rsaUtil.decrypt(loginRequest.password)
        val member = memberCrudService.findByAuthId(loginRequest.authId)

        if (!BCryptPasswordEncoder().matches(decryptedPassword, member.password)) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }

        // 토큰 발급
        val accessToken = JwtUtil.generateAccessToken(member.authId)
        val refreshToken = JwtUtil.generateRefreshToken(member.authId)

        // DB에 Refresh Token 저장
        memberService.updateRefreshToken(member.authId, refreshToken)
        val memberDetailsDto = memberService.loadInitialMemberInfos(TEST_USER_AUTH_ID)

        val loginResponse = LoginResponse(
            token = accessToken,      // Access Token
            refreshToken = refreshToken,
            memberDetailsDto = memberDetailsDto
        )

        logger.info { "login response: $loginResponse" }

        return loginResponse
    }

    // Refresh Token으로 Access Token 재발급
    fun refreshAccessToken(refreshToken: String): String {
        if (!JwtUtil.validateToken(refreshToken)) {
            throw IllegalArgumentException("Refresh Token 이 유효하지 않음.")
        }

        val authId = JwtUtil.extractAuthId(refreshToken)
            ?: throw IllegalArgumentException("토큰에서 사용자를 추출할 수 없음.")

        val member = memberCrudService.findByAuthId(authId)

        // DB에 저장된 Refresh Token과 비교(재발급 시도한 토큰이 맞는지 확인)
        if (member.refreshToken != refreshToken) {
            throw IllegalArgumentException("이미 만료되었거나 유효하지 않은 Refresh Token.")
        }

        // 새 Access Token 발급
        return JwtUtil.generateAccessToken(member.authId)
    }

    companion object : KLogging() {
        const val TEST_USER_AUTH_ID = "dummyId1"
    }
}