package com.bogu.service.crud

import com.bogu.domain.entity.postgresql.ChatRoom
import com.bogu.repo.postgresql.ChatRoomRepository
import com.bogu.util.RoomId
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ChatRoomCrudService(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatMessageCrudService: ChatMessageCrudService,
) {
    fun findChatRoomIdsBy(memberId: Long): List<Long> {
        return chatRoomRepository.findChatRoomIdsBy(memberId)
    }

    fun findChatRoomsBy(chatRoomIds: List<Long>, excludeMemberId: Long): List<ChatRoom> {
        return chatRoomRepository.findChatRoomsBy(chatRoomIds, excludeMemberId)
    }

    @Transactional
    fun createOrUpdateDirectChatRoom(initiatorId: Long, respondentId: Long): RoomId {
        require(initiatorId != respondentId) {
            "1:1 chat is not possible between the same users."
        }

        val existingRoomId = chatRoomRepository
            .findDirectChatRoomBy(initiatorId, respondentId)
        if (existingRoomId != null) {
            chatRoomRepository.updateModifiedAtById(existingRoomId)
            chatMessageCrudService.saveRoomUpdatedMessage(existingRoomId)
            return existingRoomId
        }

        val newRoom = ChatRoom(
            name = "Direct Chat($initiatorId, $respondentId)"
        )

        val newRoomId = chatRoomRepository.save(newRoom).id
        chatMessageCrudService.saveRoomCreatedMessage(newRoomId)
        return newRoomId
    }
}