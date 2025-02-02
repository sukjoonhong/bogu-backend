package com.bogu.service.crud

import com.bogu.domain.entity.postgresql.Member
import com.bogu.repo.postgresql.MemberRepository
import jakarta.transaction.Transactional
import mu.KLogging
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class MemberCrudService(
    private val memberRepository: MemberRepository,
) {
    fun findMemberDetailsByAuthId(authId: String): Member {
        try {
            // spring security 가 UsernameNotFoundException 내부적으로 처리해서
            // try-catch 안 묶으면 exception 먹음
            return memberRepository.findMemberDetailsByAuthId(authId)
                ?: throw UsernameNotFoundException("member not found for authId: $authId")
        } catch (e: UsernameNotFoundException) {
            logger.error(e) { "authentication failed: " }
            throw e
        }
    }

    fun findAll(): List<Member> {
        return memberRepository.findAll()
    }

    fun findByAuthId(authId: String): Member? {
        return memberRepository.findByAuthId(authId)
    }

    @Transactional
    fun deleteByAuthId(authId: String) {
        memberRepository.deleteByAuthId(authId)
    }

    companion object : KLogging()
}