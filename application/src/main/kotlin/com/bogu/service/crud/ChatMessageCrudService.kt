package com.bogu.service.crud

import com.bogu.domain.ChatMessageType
import com.bogu.domain.dto.ChatMessageDto
import com.bogu.repo.postgresql.ChatMessageRepository
import com.bogu.util.RoomId
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatMessageCrudService(
    private val chatMessageRepository: ChatMessageRepository,
) {
    @Transactional
    fun saveRoomCreatedMessage(roomId: RoomId) {
        chatMessageRepository.insertChatMessage(
            type = ChatMessageType.ROOM_CREATE.jsonValue.uppercase(Locale.getDefault()),
            chatRoomId = roomId,
            senderId = 51,
            content = "SYSTEM: created room"
        )
    }

    @Transactional
    fun saveRoomUpdatedMessage(roomId: RoomId) {
        chatMessageRepository.insertChatMessage(
            type = ChatMessageType.ROOM_UPDATE.jsonValue.uppercase(Locale.getDefault()),
            chatRoomId = roomId,
            senderId = 51,
            content = "SYSTEM: updated room"
        )
    }

    @Transactional
    fun save(chatMessageDto: ChatMessageDto) {
        chatMessageRepository.insertChatMessage(
            chatRoomId = chatMessageDto.roomId,
            content = chatMessageDto.content,
            senderId = chatMessageDto.senderId,
            type = chatMessageDto.type.jsonValue.uppercase(Locale.getDefault()),
        )
    }
}