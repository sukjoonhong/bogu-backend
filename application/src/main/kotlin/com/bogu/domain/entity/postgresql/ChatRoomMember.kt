package com.bogu.domain.entity.postgresql


import jakarta.persistence.*
import org.hibernate.annotations.Where

@Entity
@Table(
    name = "chat_room_member",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq__chat_room_member__chat_room_id_member_id",
            columnNames = ["chat_room_id", "member_id"]
        )
    ],
    indexes = [
        Index(name = "idx__chat_room_member__member_id", columnList = "member_id"),
        Index(name = "idx__chat_room_member__chat_room_id", columnList = "chat_room_id")
    ]
)
data class ChatRoomMember(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "chat_room_id",
        foreignKey = ForeignKey(name = "fk__chat_room_member__chat_room_id")
    )
    @Where(clause = "deleted = false")
    val chatRoom: ChatRoom,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "member_id",
        foreignKey = ForeignKey(name = "fk__chat_room_member__member_id")
    )
    val member: Member,

) : BaseEntity()

