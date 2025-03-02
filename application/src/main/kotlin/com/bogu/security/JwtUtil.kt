package com.bogu.security

import com.bogu.util.TimeUnit
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.*
import javax.crypto.SecretKey

object JwtUtil {
    // 시연용 키. 실제 서비스에서는 환경변수나 Secret Manager 등으로 안전하게 관리할 것.
    private const val SECRET_KEY = "your-256-bit-secret-your-256-bit-secret" // 32자 이상 문자열
    private val key: SecretKey = Keys.hmacShaKeyFor(SECRET_KEY.toByteArray())

    // 만료 시간(예시)
    private val ACCESS_TOKEN_EXPIRATION = TimeUnit.MINUTE.toMillis(15)  // 15분
    private val REFRESH_TOKEN_EXPIRATION = TimeUnit.DAY.toMillis(14)

    // Access Token 생성
    fun generateAccessToken(authId: String): String {
        return Jwts.builder()
            .subject(authId)
            .issuedAt(Date()) //발급 시각
            .expiration(Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
            .signWith(key)
            .compact()
    }

    // Refresh Token 생성
    fun generateRefreshToken(authId: String): String {
        return Jwts.builder()
            .subject(authId)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
            .signWith(key)
            .compact()
    }

    // 토큰 유효성 검증
    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    // 토큰에서 authId(주체) 추출
    fun extractAuthId(token: String): String? {
        return try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload.subject
        } catch (e: Exception) {
            null
        }
    }
}