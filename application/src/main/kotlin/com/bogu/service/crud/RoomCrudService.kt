package com.bogu.service.crud

import com.bogu.domain.dto.ChatMessage
import com.bogu.repo.postgresql.RoomRepository
import com.bogu.util.RoomId
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class RoomCrudService(
    private val roomRepository: RoomRepository,
) {
    @Transactional
    fun getOrCreate(chatMessage: ChatMessage): RoomId {
        return createRoomIfNotExists(chatMessage.sender, chatMessage.receiver)
            ?: chatMessage.roomId
    }

    @Transactional
    fun getOrCreate(leftWingId: Long, rightWingId: Long): RoomId {
        val (left, right) = getSortedPair(leftWingId, rightWingId)
        return roomRepository.findRoomIdBy(left, right)
            ?: roomRepository.insert(left, right)
    }

    @Transactional
    fun deleteBy(leftWingId: Long, rightWingId: Long) {
        val (left, right) = getSortedPair(leftWingId, rightWingId)
        roomRepository.deleteBy(left, right)
    }

    private fun createRoomIfNotExists(leftWingId: Long, rightWingId: Long): RoomId? {
        val (left, right) = getSortedPair(leftWingId, rightWingId)
        return roomRepository.insertIfNotExists(left, right)
    }

    private fun getSortedPair(leftWingId: Long, rightWingId: Long): Pair<Long, Long> {
        val left = listOf(leftWingId, rightWingId).minOf { it }
        val right = listOf(leftWingId, rightWingId).maxOf { it }
        return Pair(left, right)
    }
}