package com.bogu.repo.postgresql


import com.bogu.domain.dto.ChatMessageDto.Companion.DEFAULT_FETCH_SIZE
import com.bogu.domain.entity.postgresql.ChatMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
    @Modifying
    @Query(
        value = """
        INSERT INTO chat_message
        (type, chat_room, sender, content, created_at, modified_at)
        VALUES (:type, :chatRoomId, :senderId, :content, CURRENT_TIMESTAMP AT TIME ZONE 'UTC', CURRENT_TIMESTAMP AT TIME ZONE 'UTC')
        """,
        nativeQuery = true
    )
    fun insertChatMessage(
        @Param("type") type: String,
        @Param("chatRoomId") chatRoomId: Long,
        @Param("senderId") senderId: Long,
        @Param("content") content: String
    ): Int


    @Query(
        value = """
        SELECT *
          FROM (
            SELECT cm.*,
                   ROW_NUMBER() OVER (PARTITION BY cm.chat_room ORDER BY cm.id DESC) AS rn
              FROM chat_message cm
             WHERE cm.chat_room IN (:chatRoomIds)
          ) t
        WHERE t.rn <= :size
        """,
        nativeQuery = true
    )
    fun findLatestMessagesByChatRooms(
        @Param("chatRoomIds") chatRoomIds: List<Long>,
        @Param("size") size: Int = DEFAULT_FETCH_SIZE
    ): List<ChatMessage>

    @Query(
        value = """
            SELECT cm.*
              FROM chat_message cm
            WHERE cm.chat_room = :chatRoomId
            ORDER BY cm.id DESC
            LIMIT :limit OFFSET :offset
        """,
        nativeQuery = true
    )
    fun findMessagesByChatRoom(
        @Param("chatRoomId") chatRoomId: Long,
        @Param("offset") offset: Int,
        @Param("limit") limit: Int
    ): List<ChatMessage>
}