package com.bogu.service.crud

import com.bogu.repo.postgresql.ChatRoomMemberRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ChatRoomMemberCrudService(
    private val chatRoomMemberRepository: ChatRoomMemberRepository
) {
    @Transactional
    fun createChatRoomMembers(roomId: Long, memberIds: List<Long>) {
        chatRoomMemberRepository.upsertChatRoomMembers(roomId, memberIds.toTypedArray())
    }
}