package com.bogu.repo.postgresql

import com.bogu.domain.entity.postgresql.Member
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository: BaseRepository<Member, Long> {
    fun findByAuthId(@Param("authId") authId: String): Member?

    @Query(value = """
        SELECT m.*
        FROM caree_care_giver ccg
        JOIN member m on ccg.care_giver = m.id
        WHERE ccg.caree = :careeId
          AND ccg.status in (:statuses)
    """, nativeQuery = true)
    fun findCareGiverMembersBy(
        @Param("careeId") careeId: Long,
        @Param("statuses") statuses: List<String>
    ): List<Member>?
}