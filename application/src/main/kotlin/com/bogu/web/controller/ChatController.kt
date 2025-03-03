package com.bogu.web.controller

import com.bogu.domain.dto.ChatMessageDto.Companion.DEFAULT_FETCH_SIZE
import com.bogu.domain.dto.ChatMessageDto.Companion.FETCH_SIZE
import com.bogu.domain.dto.response.ChatMessagePageDto
import com.bogu.service.chat.ChatService
import com.bogu.service.crud.ChatMessageCrudService
import com.bogu.util.RoomId
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chat")
class ChatController(
    private val chatService: ChatService,
    private val chatMessageCrudService: ChatMessageCrudService
) {
    @Deprecated("Not used on the client.")
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

    @GetMapping("/rooms/{chatRoomId}/messages")
    fun getChatMessages(
        @PathVariable chatRoomId: Long,
        @RequestParam offset: Int = DEFAULT_FETCH_SIZE,
        @RequestParam limit: Int = FETCH_SIZE
    ): ChatMessagePageDto {
        return chatMessageCrudService.findNextMessagesByChatRoom(
            chatRoomId = chatRoomId,
            offset = offset,
            limit = limit
        )
    }
}