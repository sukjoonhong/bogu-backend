package com.bogu.domain.entity.postgresql

import jakarta.persistence.*

@Entity
@Table(
    name = "chat_message",
    indexes = [
        Index(name = "idx__chat_message__chat_room", columnList = "chat_room_id")
    ]
)
data class ChatMessage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "chat_room_id",
        foreignKey = ForeignKey(name = "fk__chat_message__chat_room"),
        nullable = false
    )
    val chatRoom: ChatRoom,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "sender_id",
        foreignKey = ForeignKey(name = "fk__chat_message__sender"),
        nullable = false
    )
    val sender: Member,

    @Column(nullable = false, columnDefinition = "TEXT")
    val content: String,
) : BaseEntity()