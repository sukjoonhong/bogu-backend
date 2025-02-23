package com.bogu.domain.entity.postgresql


import com.bogu.domain.ChatMessageType
import com.bogu.domain.dto.ChatMessageDto
import jakarta.persistence.*
import java.time.format.DateTimeFormatter

@Entity
@Table(
    name = "chat_message",
    indexes = [
        Index(name = "idx__chat_message__chat_room", columnList = "chat_room")
    ]
)
data class ChatMessage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    val type: ChatMessageType,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "chat_room",
        foreignKey = ForeignKey(name = "fk__chat_message__chat_room"),
        nullable = false
    )
    val chatRoom: ChatRoom,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "sender",
        foreignKey = ForeignKey(name = "fk__chat_message__sender"),
        nullable = false
    )
    val sender: Member,

    @Column(nullable = false, columnDefinition = "TEXT")
    val content: String,
) : BaseEntity()

fun ChatMessage.toDto(roomId: Long): ChatMessageDto {
    return ChatMessageDto(
        type = this.type,
        roomId = roomId,
        senderId = this.sender.id,
        content = this.content,
        createdAt = this.createdAt.format(DateTimeFormatter.ISO_DATE_TIME)
    )
}