package com.bogu.service

import com.bogu.service.crud.CareeCareGiverCrudService
import com.bogu.util.RoomId
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class MatchingService(
    private val careeCareGiverCrudService: CareeCareGiverCrudService,
    private val chatService: ChatService,
) {
    fun confirmMatchingAndCreateChatRoom(careeId: Long, careGiverId: Long): RoomId {
        createMatching(careeId, careGiverId)
        return chatService.createOrUpdateDirectChatRoom(careeId, careGiverId)
    }

    fun createMatching(careeId: Long, careGiverId: Long) {
        careeCareGiverCrudService.createMatching(careeId, careGiverId)
        logger.info { "creating matching for caree $careeId, caregiver $careGiverId" }
    }

    fun createRequestMatching(careeId: Long, careGiverId: Long) {
        careeCareGiverCrudService.createRequestMatching(careeId, careGiverId)
        logger.info { "creating request matching for caree $careeId, caregiver $careGiverId" }
    }

    fun cancelRequestMatching(careeId: Long, careGiverId: Long) {
        careeCareGiverCrudService.cancelRequestMatching(careeId, careGiverId)
    }

    companion object: KLogging()
}