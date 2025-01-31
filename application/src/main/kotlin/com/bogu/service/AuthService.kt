package com.bogu.service

import com.bogu.domain.dto.request.LoginRequest
import com.bogu.domain.dto.response.LoginResponse
import com.bogu.domain.entity.postgresql.Member
import com.bogu.domain.entity.postgresql.toDto
import com.bogu.service.crud.MemberCrudService
import mu.KLogging
import org.jetbrains.annotations.TestOnly
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberCrudService: MemberCrudService
) {
    fun login(loginRequest: LoginRequest): LoginResponse {
        logger.info { "login request: $loginRequest" }
        val loginResponse = LoginResponse(
            id = TEST_USER_ID,
            token = "dfdf",
            careGiverCandidates = get5MembersRandomly().map { it.toDto() },
        )
        logger.info { "login response: $loginResponse" }
        return loginResponse
    }

    @TestOnly
    private fun get5MembersRandomly(): List<Member> {
        return memberCrudService.findAll().filter { it.id != TEST_USER_ID }.shuffled().subList(0, 4)
    }

    companion object : KLogging() {
        const val TEST_USER_ID = 1L
    }
}