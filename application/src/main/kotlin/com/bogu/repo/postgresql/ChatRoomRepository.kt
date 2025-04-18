package com.bogu.repo.postgresql

import com.bogu.domain.ChatRoomType
import com.bogu.domain.entity.postgresql.ChatRoom
import com.bogu.util.RoomId
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ChatRoomRepository : BaseRepository<ChatRoom, Long> {
    /**
     * 엔티티 변경시 쿼리 성능 주의
     */
    @Query(
        """
        SELECT DISTINCT cr
        FROM ChatRoom cr
        LEFT JOIN FETCH cr.members crm
        LEFT JOIN FETCH crm.member m
        WHERE cr.id IN :chatRoomIds
          AND m.id != :excludeMemberId
    """
    )
    fun findChatRoomsBy(
        @Param("chatRoomIds") chatRoomIds: List<Long>,
        @Param("excludeMemberId") excludeMemberId: Long
    ): List<ChatRoom>

    @Query(
        value = """
        SELECT DISTINCT cr.id
        FROM chat_room cr
        JOIN chat_room_member crm on cr.id = crm.chat_room_id
        WHERE crm.member_id = :memberId
    """, nativeQuery = true
    )
    fun findChatRoomIdsBy(@Param("memberId") memberId: Long): List<Long>

    @Query(
        """
        SELECT cr.id
        FROM ChatRoom cr
        JOIN cr.members crm1
        JOIN cr.members crm2
        WHERE cr.type = :type
          AND crm1.member.id = :initiatorId
          AND crm2.member.id = :respondentId
    """
    )
    fun findDirectChatRoomBy(
        @Param("initiatorId") initiatorId: Long,
        @Param("respondentId") respondentId: Long,
        @Param("type") type: ChatRoomType = ChatRoomType.DIRECT
    ): RoomId?

    @Modifying
    @Query(
        value = """
        UPDATE chat_room 
        SET modified_at = CURRENT_TIMESTAMP AT TIME ZONE 'UTC' 
        WHERE id = :chatRoomId
    """,
        nativeQuery = true
    )
    fun updateModifiedAtById(chatRoomId: Long)

    @Query(
        value = """
        SELECT cm.content
        FROM chat_room cr
                 JOIN chat_message cm on cr.id = cm.chat_room
        WHERE cm.type = 'CHAT'
          AND cr.id = :chatRoomId
        ORDER BY cm.id desc
        LIMIT 1
    """,
        nativeQuery = true
    )
    fun findLastChatMessageBy(chatRoomId: Long): String?
}