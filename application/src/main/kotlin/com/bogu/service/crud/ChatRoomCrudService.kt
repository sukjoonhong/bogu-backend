package com.bogu.service.crud

import com.bogu.domain.entity.postgresql.ChatRoom
import com.bogu.repo.postgresql.ChatRoomRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ChatRoomCrudService(
    private val chatRoomRepository: ChatRoomRepository,
) {
    fun findChatRoomIdsBy(memberId: Long): List<Long> {
        return chatRoomRepository.findChatRoomIdsBy(memberId)
    }

    fun findChatRoomsBy(chatRoomIds: List<Long>, excludeMemberId: Long): List<ChatRoom> {
        return chatRoomRepository.findChatRoomsBy(chatRoomIds, excludeMemberId)
    }

    @Transactional
    fun createOrGetDirectChatRoom(initiatorId: Long, respondentId: Long): Long {
        require(initiatorId != respondentId) {
            "1:1 chat is not possible between the same users."
        }

        val existingRoomId = chatRoomRepository
            .findDirectChatRoomBy(initiatorId, respondentId)
        if (existingRoomId != null) {
            return existingRoomId
        }

        val newRoom = ChatRoom(
            name = "Direct Chat($initiatorId, $respondentId)"
        )
        return chatRoomRepository.save(newRoom).id
    }
}