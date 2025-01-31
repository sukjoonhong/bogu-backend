package com.bogu.repo.postgresql

import com.bogu.domain.entity.postgresql.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository: JpaRepository<Member, Long> {
    fun findByAuthId(authId: String): Member?

    fun deleteByAuthId(authId: String)
}