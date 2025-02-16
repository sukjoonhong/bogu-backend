package com.bogu.repo.postgresql

import com.bogu.domain.entity.postgresql.CareeCareGiver
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CareeCareGiverRepository : JpaRepository<CareeCareGiver, Long> {
    @Modifying
    @Query(
        value = """
            INSERT INTO caree_care_giver (caree, care_giver, status, created_at, modified_at)
            VALUES (:careeId, :careGiverId, 'REQUESTED', CURRENT_TIMESTAMP AT TIME ZONE 'UTC', CURRENT_TIMESTAMP AT TIME ZONE 'UTC')
            ON CONFLICT (caree, care_giver)
                DO UPDATE
                SET status      = CASE
                                      WHEN caree_care_giver.status = 'CANCELED'
                                          THEN 'REQUESTED'
                                      ELSE caree_care_giver.status
                    END,
                    modified_at = CASE
                                      WHEN caree_care_giver.status = 'CANCELED'
                                          THEN CURRENT_TIMESTAMP AT TIME ZONE 'UTC'
                                      ELSE caree_care_giver.modified_at
                    END
                    WHERE caree_care_giver.status != 'CONTRACTED'
        """,
        nativeQuery = true
    )
    fun createOrReactivateRequestMatching(
        @Param("careeId") careeId: Long,
        @Param("careGiverId") careGiverId: Long
    )

    @Modifying
    @Query(
        value = """
            UPDATE caree_care_giver 
            SET status = :status
            WHERE caree = :careeId 
              AND care_giver = :careGiverId
              AND status != 'CONTRACTED'
        """,
        nativeQuery = true
    )
    fun updateStatusBy(
        @Param("careeId") careeId: Long,
        @Param("careGiverId") careGiverId: Long,
        @Param("status") status: String
    )
}