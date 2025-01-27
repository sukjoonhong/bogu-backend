package com.bogu.bogubackend.domain.entity.postgresql

import jakarta.persistence.*


@Entity
class Room private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(
        name = "left_wing",
        foreignKey = ForeignKey(name = "fk_room_left_wing"),
    )
    val leftWing: Member? = null,

    @ManyToOne
    @JoinColumn(
        name = "right_wing",
        foreignKey = ForeignKey(name = "fk_room_right_wing"),
    )
    val rightWing: Member? = null,
) {
    // JPA 기본생성자
    protected constructor() : this(0, null, null)

    constructor(left: Member, right: Member) : this(
        leftWing = if (left.id < right.id) left else right,
        rightWing = if (left.id < right.id) right else left
    )

    init {
        if (leftWing != null && rightWing != null) {
            require(leftWing!!.id < rightWing!!.id) {
                "leftWing must have a smaller id than rightWing"
            }
        }
    }

    fun copy(
        id: Long? = this.id,
        leftWing: Member? = this.leftWing,
        rightWing: Member? = this.rightWing
    ): Room {
        return Room(
            id = id ?: this.id,
            leftWing = leftWing ?: this.leftWing,
            rightWing = rightWing ?: this.rightWing
        )
    }
}