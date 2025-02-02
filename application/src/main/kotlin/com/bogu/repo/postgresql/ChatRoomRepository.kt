package com.bogu.repo.postgresql

import com.bogu.domain.entity.postgresql.ChatRoom
import com.bogu.util.RoomId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ChatRoomRepository : JpaRepository<ChatRoom, Long> {
    @Modifying
    @Query(
        value = """
        INSERT INTO chat_room (sender, receiver, deleted, created_at, modified_at)
        VALUES (:senderId, :receiverId, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), (:receiverId, :senderId, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
        ON CONFLICT (sender, receiver)
        DO UPDATE 
        SET deleted = false,
            modified_at = CURRENT_TIMESTAMP
        WHERE chat_room.deleted = true
    """,
        nativeQuery = true
    )
    fun insertOrUpdatePairedChatRoomsIfDeleted(
        @Param("senderId") senderId: Long,
        @Param("receiverId") receiverId: Long
    ): Int

    @Query(
        value = """
            SELECT cr.id 
            FROM ChatRoom cr 
            WHERE cr.sender.id = :senderId 
                AND cr.receiver.id = :receiverId
        """
    )
    fun findRoomIdBy(senderId: Long, receiverId: Long): RoomId?

    @Modifying
    @Query(
        value = """
        UPDATE chat_room 
        SET deleted = true
        WHERE sender = :senderId 
          AND receiver = :receiverId
          AND deleted = false
    """,
        nativeQuery = true
    )
    fun softDeleteBy(
        @Param("senderId") senderId: Long,
        @Param("receiverId") receiverId: Long
    )
}