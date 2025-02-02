package com.bogu.domain.entity.postgresql

import com.bogu.domain.dto.MemberDto
import com.bogu.domain.dto.ProfileDto
import jakarta.persistence.*
import org.hibernate.annotations.Where
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

    // 실명 인증 완료 시각
    val realNameVerifiedAt: LocalDateTime? = null,

    // 휴대폰 번호 (선택사항)
    val phoneNumber: String? = null,

    // 생년월일 (선택사항)
    val birthDate: LocalDate? = null,

    // 인증 제공자 (SSO)
    val provider: String? = null,

    // 해당 provider 에서 사용하는 유저 식별자
    val providerId: String? = null

) : BaseEntity() {
    @OneToOne(mappedBy = "member")
    var profile: Profile? = null

    @OneToMany(
        mappedBy = "sender",
        fetch = FetchType.LAZY,
    )
    @Where(clause = "deleted = false")
    val chatRooms: Set<ChatRoom> = emptySet()

    @OneToMany(
        mappedBy = "caree",
        fetch = FetchType.LAZY,
    )
    @Where(clause = "deleted = false")
    val careeCareGivers: Set<CareeCareGiver> = emptySet()

    fun getCareGiverCandidates(): List<Member> {
        return careeCareGivers.map { it.careGiver }
    }
}

fun Member.toDto(): MemberDto {
    return MemberDto(
        id = this.id,
        name = this.name,
        nickName = this.nickName,
        profile = this.profile?.let {
            ProfileDto(
                id = it.id,
            )
        },
    )
}