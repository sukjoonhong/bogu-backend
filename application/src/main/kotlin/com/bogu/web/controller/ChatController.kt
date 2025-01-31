package com.bogu.web.controller

import com.bogu.service.ChatService
import com.bogu.util.RoomId
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chat")
class ChatController(
    private val chatService: ChatService
) {

    @PostMapping("/rooms/create")
    fun create(
        @RequestParam(value = "senderId", required = true) senderId: Long,
        @RequestParam(value = "receiverId", required = true) receiverId: Long,
    ): RoomId {
        return chatService.getOrCreateRoom(
            leftWingId = senderId,
            rightWingId = receiverId
        )
    }
}