package com.bogu.service.crud

import com.bogu.domain.entity.postgresql.ChatRoom
import com.bogu.repo.postgresql.ChatRoomRepository
import com.bogu.util.RoomId
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ChatRoomCrudService(
    private val chatRoomRepository: ChatRoomRepository,
) {
    @Transactional
    fun createPairedChatRooms(senderId: Long, receiverId: Long) {
        chatRoomRepository.insertOrUpdatePairedChatRoomsIfDeleted(senderId, receiverId)
    }

    fun findRoomIdBy(senderId: Long, receiverId: Long): RoomId? {
        return chatRoomRepository.findRoomIdBy(senderId, receiverId)
    }

    @Transactional
    fun softDeleteBy(senderId: Long, receiverId: Long) {
        chatRoomRepository.softDeleteBy(senderId, receiverId)
    }
}