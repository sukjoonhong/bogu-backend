package com.bogu.repo.postgresql


import com.bogu.domain.entity.postgresql.ChatMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
}