package com.bogu.service.crud

import com.bogu.domain.CareeCareGiverStatus
import com.bogu.domain.entity.postgresql.Member
import com.bogu.repo.postgresql.MemberRepository
import mu.KLogging
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class MemberCrudService(
    private val memberRepository: MemberRepository,
) {
    fun findCareGiverCandidates(careeId: Long): List<Member>? {
        return memberRepository.findCareGiverMembersBy(
            careeId,
            CareeCareGiverStatus.initialVisibleStatuses()
        )
    }

    fun findByAuthId(authId: String): Member {
        try {
            // spring security 가 UsernameNotFoundException 내부적으로 처리해서
            // try-catch 안 묶으면 exception 먹음
            return memberRepository.findByAuthId(authId)
                ?: throw UsernameNotFoundException("member not found for authId: $authId")
        } catch (e: UsernameNotFoundException) {
            logger.error(e) { "authentication failed: " }
            throw e
        }
    }

    companion object : KLogging()
}