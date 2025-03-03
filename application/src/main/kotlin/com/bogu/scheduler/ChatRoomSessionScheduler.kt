package com.bogu.scheduler

import com.bogu.service.chat.ChatRoomSessionService
import mu.KLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ChatRoomSessionScheduler(
    private val chatRoomSessionService: ChatRoomSessionService,
) {
    @Scheduled(cron = "0 0 2 * * ?")
    fun sync() {
        chatRoomSessionService.sync()
    }

    companion object: KLogging()
}