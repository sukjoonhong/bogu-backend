package com.bogu.domain.entity.postgresql

import jakarta.persistence.*


@Entity
@Table(
    name = "chat_room",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq__chat_room__sender_receiver",
            columnNames = ["sender", "receiver"]
        ),
    ],
)
data class ChatRoom(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(
        optional = false,
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "sender",
        foreignKey = ForeignKey(name = "fk__chat_room__sender"),
    )
    val sender: Member,

    val deleted: Boolean = false,

    @ManyToOne(
        optional = false,
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "receiver",
        foreignKey = ForeignKey(name = "fk__chat_room__receiver"),
    )
    val receiver: Member,
): BaseEntity() {
    // ChatMessage 와의 관계
    @OneToMany(
        mappedBy = "chatRoom",
        fetch = FetchType.LAZY,
    )
    val chatMessages: Set<ChatMessage> = emptySet()
}