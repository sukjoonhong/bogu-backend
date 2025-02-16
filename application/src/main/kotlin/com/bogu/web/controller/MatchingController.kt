package com.bogu.web.controller

import com.bogu.service.MatchingService
import com.bogu.util.RoomId
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/matching")
class MatchingController(
    private val matchingService: MatchingService
) {
    @PostMapping("/requests/{careeId}")
    fun createRequestMatching(
        @PathVariable careeId: Long,
        @RequestParam(value = "careGiverId", required = true) careGiverId: Long,
    ) {
        require(careeId != careGiverId) {
            "careeId and careGiverId cannot be the same."
        }
        matchingService.createRequestMatching(careeId, careGiverId)
    }

    @DeleteMapping("/requests/{careeId}")
    fun deleteRequestMatching(
        @PathVariable careeId: Long,
        @RequestParam(value = "careGiverId", required = true) careGiverId: Long,
    ) {
        require(careeId != careGiverId) {
            "careeId and careGiverId cannot be the same."
        }
        matchingService.cancelRequestMatching(careeId, careGiverId)
    }

    @Deprecated("Not used on the client.")
    @PostMapping("/{careeId}")
    fun createMatching(
        @PathVariable careeId: Long,
        @RequestParam(value = "careGiverId", required = true) careGiverId: Long,
    ) {
        require(careeId != careGiverId) {
            "careeId and careGiverId cannot be the same."
        }
        matchingService.createMatching(careeId, careGiverId)
    }

    @PostMapping("/{initiatorId}/chat")
    fun confirmMatchingAndCreateChatRoom(
        @PathVariable initiatorId: Long,
        @RequestParam(value = "respondentId", required = true) respondentId: Long,
    ): RoomId {
        require(initiatorId != respondentId) {
            "senderId and receiverId cannot be the same."
        }

        return matchingService.confirmMatchingAndCreateChatRoom(
            careeId = initiatorId,
            careGiverId = respondentId
        )
    }
}