package com.bogu.repo.postgresql

import com.bogu.domain.entity.postgresql.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProfileRepository : JpaRepository<Profile, Long> {
    @Query(
        value = """
        SELECT p.*
        FROM profile p
        WHERE p.member_id in (:memberIds)
    """, nativeQuery = true
    )
    fun findProfilesBy(@Param("memberIds") memberIds: List<Long>): List<Profile>?
}