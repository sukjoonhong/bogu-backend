package com.bogu.bogubackend.repo.postgresql

import com.bogu.bogubackend.domain.entity.postgresql.Member
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository: JpaRepository<Member, Long> {
    fun findByMemberId(memberId: String): Member?

    @Transactional
    fun deleteByMemberId(memberId: String)
}