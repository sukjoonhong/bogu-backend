package com.bogu.domain.entity.postgresql

import jakarta.persistence.*

@Entity
data class Profile(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val bio: String = "",
    val description: String = "",
    val location: String = "",

    @OneToOne(
        optional = false,
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "member_id",
        foreignKey = ForeignKey(name = "fk__profile__member"),
    )
    val member: Member,
): BaseEntity()
