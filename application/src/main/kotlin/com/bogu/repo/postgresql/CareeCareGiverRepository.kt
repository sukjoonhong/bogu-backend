package com.bogu.repo.postgresql

import com.bogu.domain.entity.postgresql.CareeCareGiver
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CareeCareGiverRepository: JpaRepository<CareeCareGiver, Long> {
    @Modifying
    @Query(
        value = """
            INSERT INTO caree_care_giver (caree, care_giver, deleted, created_at, modified_at)
            VALUES (?, ?, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            ON CONFLICT (caree, care_giver) 
            DO UPDATE 
            SET deleted = false, 
                modified_at = CURRENT_TIMESTAMP
            WHERE caree_care_giver.deleted = true;
        """,
        nativeQuery = true
    )
    fun insertIfNotExists(
        @Param("careeId") careeId: Long,
        @Param("careGiverId") careGiverId: Long
    )

    @Modifying
    @Query(
        value = """
        UPDATE caree_care_giver 
        SET deleted = true
        WHERE caree = :careeId 
          AND care_giver = :careGiverId
          AND deleted = false
    """,
        nativeQuery = true
    )
    fun softDeleteBy(
        @Param("careeId") careeId: Long,
        @Param("careGiverId") careGiverId: Long
    )
}