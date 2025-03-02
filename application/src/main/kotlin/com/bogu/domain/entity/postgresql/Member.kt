package com.bogu.domain.entity.postgresql

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(
    name = "member",
    uniqueConstraints = [
        UniqueConstraint(name = "uq__member__auth_id", columnNames = ["auth_id"])
    ],
    indexes = [
        Index(name = "idx__member__auth_id", columnList = "auth_id")
    ]
)
data class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "auth_id")
    val authId: String,
    val name: String,
    val nickName: String,
    val password: String,

    val isRealNameVerified: Boolean = false,
    val realNameVerifiedAt: LocalDateTime? = null,
    val phoneNumber: String? = null,
    val birthDate: LocalDate? = null,
    val provider: String? = null,
    val providerId: String? = null,

    val refreshToken: String? = null
) : BaseEntity() {
}