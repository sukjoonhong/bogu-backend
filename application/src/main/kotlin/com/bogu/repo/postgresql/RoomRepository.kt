package com.bogu.repo.postgresql

import com.bogu.domain.entity.postgresql.Room
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RoomRepository: JpaRepository<Room, Long> {
    @Modifying
    @Query(
        value = """
            INSERT INTO room (left_wing, right_wing)
            VALUES (:leftWingId, :rightWingId)
            ON CONFLICT (left_wing, right_wing) DO NOTHING
        """,
        nativeQuery = true
    )
    fun insertIfNotExists(
        @Param("leftWingId") leftWingId: Long,
        @Param("rightWingId") rightWingId: Long
    ): Long?
}