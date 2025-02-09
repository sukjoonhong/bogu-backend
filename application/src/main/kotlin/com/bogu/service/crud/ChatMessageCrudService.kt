package com.bogu.service.crud

import com.bogu.domain.dto.ChatMessageDto
import com.bogu.domain.entity.postgresql.ChatMessage
import com.bogu.repo.postgresql.ChatMessageRepository
import jakarta.transaction.Transactional
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatMessageCrudService(
    private val chatMessageRepository: ChatMessageRepository,
) {
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