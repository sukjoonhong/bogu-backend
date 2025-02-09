package com.bogu.web.controller

import com.bogu.service.ChatService
import com.bogu.util.RoomId
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chat")
class ChatController(
    private val chatService: ChatService
) {
    @PostMapping("/rooms/direct")
    fun createOrUpdateDirectChatRoom(
        @RequestParam(value = "initiatorId", required = true) initiatorId: Long,
        @RequestParam(value = "receiverId", required = true) respondentId: Long,
    ): RoomId {
        require(initiatorId != respondentId) {
            "senderId and receiverId cannot be the same."
        }

        return chatService.createOrUpdateDirectChatRoom(
            initiatorId = initiatorId,
            respondentId = respondentId
        )
    }
}