package com.bogu.service

import com.bogu.repo.postgresql.RoomRepository
import org.springframework.stereotype.Service

@Service
class RoomCrudService(
    private val roomRepository: RoomRepository,
) {
    fun insertIfNotExist(leftWingId: Long, rightWingId: Long): Long? {
        val left = listOf(leftWingId, rightWingId).minOf { it }
        val right = listOf(leftWingId, rightWingId).maxOf { it }
        return roomRepository.insertIfNotExists(left, right)
    }
}