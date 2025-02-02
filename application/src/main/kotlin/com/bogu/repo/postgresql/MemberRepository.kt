package com.bogu.repo.postgresql

import com.bogu.domain.entity.postgresql.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository: JpaRepository<Member, Long> {

    @Query(value = """
        SELECT DISTINCT m
        FROM Member m
        LEFT JOIN FETCH m.profile
        LEFT JOIN FETCH m.chatRooms cr
        LEFT JOIN FETCH cr.chatMessages cm
        LEFT JOIN FETCH m.careeCareGivers cc
        WHERE m.authId = :authId
            AND (cc.createdAt >= (CURRENT_DATE - $CAREE_CARE_GIVER_RETENTION_DAYS) OR cc.id IS NULL)
            AND (cm.createdAt >= (CURRENT_DATE - $CHAT_MESSAGE_RETENTION_DAYS) OR cm.id IS NULL)
        """
    )
    fun findMemberDetailsByAuthId(@Param("authId") authId: String): Member?

    companion object {
        const val CHAT_MESSAGE_RETENTION_DAYS = 180
        const val CAREE_CARE_GIVER_RETENTION_DAYS = 1
    }
}