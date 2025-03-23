package com.bogu.repo.postgresql

import com.bogu.domain.entity.postgresql.Hospital
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface HospitalRepository: JpaRepository<Hospital, Long> {
    @Query(
        value = """
            SELECT h.id || ':' || h.sido_name || ' ' || h.sigungu_name || ' ' || h.name
            FROM hospital h
            ORDER BY h.sido_name, h.sigungu_name, h.name
        """, nativeQuery = true
    )
    fun findAllNames(): List<String>
}