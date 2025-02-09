package com.bogu.repo.postgresql

import com.bogu.domain.entity.postgresql.ChatRoomMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ChatRoomMemberRepository: JpaRepository<ChatRoomMember, Long> {
    @Modifying
    @Query(value = """
        WITH member_list AS (
            SELECT unnest(:memberIds) AS mid
        )
        INSERT INTO chat_room_member (chat_room_id, member_id, created_at, modified_at)
        SELECT :chatRoomId, ml.mid, NOW(), NOW()
        FROM member_list ml
        ON CONFLICT (chat_room_id, member_id)
        DO UPDATE
            SET modified_at = EXCLUDED.modified_at
        """,
        nativeQuery = true
    )
    fun upsertChatRoomMembers(
        @Param("chatRoomId") chatRoomId: Long,
        @Param("memberIds") memberIds: Array<Long>
    ): Int
}