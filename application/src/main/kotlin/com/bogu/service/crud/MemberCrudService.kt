package com.bogu.service.crud

import com.bogu.domain.entity.postgresql.Member
import com.bogu.repo.postgresql.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class MemberCrudService(
    private val memberRepository: MemberRepository,
) {
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
}