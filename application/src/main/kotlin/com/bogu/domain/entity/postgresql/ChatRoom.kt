package com.bogu.domain.entity.postgresql

import jakarta.persistence.*


@Entity
@Table(
    name = "chat_room",
    uniqueConstraints = [
        UniqueConstraint(name = "uq__chat_room__left_right", columnNames = ["left_wing", "right_wing"])
    ],
    indexes = [
        Index(name = "idx__chat_room__left_right", columnList = "left_wing, right_wing")
    ]
)
class ChatRoom private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(
        optional = false,
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "left_wing",
        foreignKey = ForeignKey(name = "fk__chat_room__left_wing"),
    )
    val leftWing: Member,

    @ManyToOne(
        optional = false,
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "right_wing",
        foreignKey = ForeignKey(name = "fk__chat_room__right_wing"),
    )
    val rightWing: Member,
) {
    constructor(left: Member, right: Member) : this(
        leftWing = if (left.id < right.id) left else right,
        rightWing = if (left.id < right.id) right else left
    )

    init {
        require(leftWing.id < rightWing.id) {
            "leftWing must have a smaller id than rightWing"
        }
    }

    fun deepCopy(
        id: Long? = null,
        leftWing: Member? = null,
        rightWing: Member? = null
    ): ChatRoom {
        return ChatRoom(
            id = id ?: this.id,
            leftWing = leftWing ?: this.leftWing,
            rightWing = rightWing ?: this.rightWing
        )
    }
}