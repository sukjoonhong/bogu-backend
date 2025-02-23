package com.bogu.service.crud

import com.bogu.domain.ChatMessageType
import com.bogu.domain.dto.ChatMessageDto
import com.bogu.domain.dto.ChatMessageDto.Companion.DEFAULT_FETCH_SIZE
import com.bogu.domain.dto.response.ChatMessagePageDto
import com.bogu.domain.entity.postgresql.ChatMessage
import com.bogu.domain.entity.postgresql.toDto
import com.bogu.repo.postgresql.ChatMessageRepository
import com.bogu.util.RoomId
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatMessageCrudService(
    private val chatMessageRepository: ChatMessageRepository,
) {
    fun findLatest3MessagesByChatRoom(chatRoomIds: List<Long>): List<ChatMessage> {
        return chatMessageRepository.findLatestMessagesByChatRooms(
            chatRoomIds = chatRoomIds,
            size = DEFAULT_FETCH_SIZE
        )
    }

    fun findNextMessagesByChatRoom(chatRoomId: Long, limit: Int, offset: Int): ChatMessagePageDto {
        val chatMessages = chatMessageRepository.findMessagesByChatRoom(
            chatRoomId = chatRoomId,
            offset = offset,
            limit = limit + 1
        ).map { it.toDto(chatRoomId) }
            .reversed()

        val hasNext = chatMessages.size > limit
        val messagesToReturn = if (hasNext) {
            chatMessages.drop(1)
        } else {
            chatMessages
        }

        return ChatMessagePageDto(
            roomId = chatRoomId,
            chatMessages = messagesToReturn.filter { it.type.isChatMessage() },
            limit = limit,
            offset = offset + messagesToReturn.size,
            hasNext = hasNext
        )
    }

    @Transactional
    fun saveRoomCreatedMessage(roomId: RoomId) {
        chatMessageRepository.insertChatMessage(
            type = ChatMessageType.ROOM_CREATE.jsonValue.uppercase(Locale.getDefault()),
            chatRoomId = roomId,
            senderId = 51,
            content = "새로운 채팅방이 생성되었습니다."
        )
    }

    @Transactional
    fun saveRoomUpdatedMessage(roomId: RoomId, lastMessage: String) {
        chatMessageRepository.insertChatMessage(
            type = ChatMessageType.ROOM_UPDATE.jsonValue.uppercase(Locale.getDefault()),
            chatRoomId = roomId,
            senderId = 51,
            content = lastMessage
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