package com.bogu.repo.postgresql


import com.bogu.domain.entity.postgresql.ChatMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
    @Modifying
    @Query(value = """
        INSERT INTO chat_message
        (type, chat_room, sender, content, created_at, modified_at)
        VALUES (:type, :chatRoomId, :senderId, :content, NOW(), NOW())
        """,
        nativeQuery = true)
    fun insertChatMessage(
        @Param("type") type: String,
        @Param("chatRoomId") chatRoomId: Long,
        @Param("senderId") senderId: Long,
        @Param("content") content: String
    ): Int
}