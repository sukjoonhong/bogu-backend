package com.bogu.repo.postgresql

import com.bogu.domain.entity.postgresql.ChatRoom
import com.bogu.util.RoomId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RoomRepository : JpaRepository<ChatRoom, Long> {
    @Modifying
    @Query(
        value = """
            INSERT INTO chat_room (left_wing, right_wing)
            VALUES (:leftWingId, :rightWingId)
            ON CONFLICT (left_wing, right_wing) DO NOTHING
        """,
        nativeQuery = true
    )
    fun insertIfNotExists(
        @Param("leftWingId") leftWingId: Long,
        @Param("rightWingId") rightWingId: Long
    ): RoomId?

    @Modifying
    @Query(
        value = """
            INSERT INTO chat_room (left_wing, right_wing)
            VALUES (:leftWingId, :rightWingId)
        """,
        nativeQuery = true
    )
    fun insert(leftWingId: Long, rightWingId: Long): RoomId


    @Query(
        value = """
            SELECT cr.id 
            FROM ChatRoom cr 
            WHERE cr.leftWing = :leftWingId 
                AND cr.rightWing = :rightWingId
        """
    )
    fun findRoomIdBy(leftWingId: Long, rightWingId: Long): RoomId?

    @Modifying
    @Query(
        value = """
            DELETE 
            FROM chat_room 
            WHERE left_wing = :leftWingId 
                AND right_wing = :rightWingId
        """,
        nativeQuery = true
    )
    fun deleteBy(
        @Param("leftWingId") leftWingId: Long,
        @Param("rightWingId") rightWingId: Long
    )
}