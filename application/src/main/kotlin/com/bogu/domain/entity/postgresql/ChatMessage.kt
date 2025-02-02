package com.bogu.domain.entity.postgresql


import com.bogu.domain.ChatMessageType
import jakarta.persistence.*

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "receiver",
        foreignKey = ForeignKey(name = "fk__chat_message__receiver"),
        nullable = false
    )
    val receiver: Member,

    @Column(nullable = false, columnDefinition = "TEXT")
    val content: String,
) : BaseEntity()