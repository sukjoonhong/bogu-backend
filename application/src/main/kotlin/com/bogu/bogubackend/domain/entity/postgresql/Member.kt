package com.bogu.bogubackend.domain.entity.postgresql

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(
    name = "member",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["member_id"])
    ]
)
data class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "member_id")
    val memberId: String,
    val memberName: String,
    val nickName: String,
    val password: String,

    val isRealNameVerified: Boolean = false,

    // 실명 인증 완료 시각
    val realNameVerifiedAt: LocalDateTime? = null,

    // 휴대폰 번호 (선택사항)
    val phoneNumber: String? = null,

    // 생년월일 (선택사항)
    val birthDate: LocalDate? = null,

    // 인증 제공자 (SSO)
    val provider: String? = null,

    // 해당 provider 에서 사용하는 유저 식별자
    val providerId: String? = null,

) : BaseEntity()
