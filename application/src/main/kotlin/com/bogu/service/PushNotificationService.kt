package com.bogu.service

import com.bogu.domain.dto.ChatMessageDto
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class PushNotificationService {
    fun sendPushNotification(memberId: Long, chatMessage: ChatMessageDto) {
        // TODO: Not Implemented
        logger.info { "Sending push notification for $memberId" }
    }

    companion object: KLogging()
}
