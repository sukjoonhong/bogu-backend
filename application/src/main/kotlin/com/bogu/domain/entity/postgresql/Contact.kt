package com.bogu.domain.entity.postgresql

import jakarta.persistence.*

@Entity
@Table(
    name = "contact",
)
data class Contact(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    val price: Long,

    @Column(nullable = false, length = 10)
    val currency: String, // 통화 (예: KRW, USD)

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    val paymentMethod: PaymentMethod,

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    val paymentStatus: PaymentStatus,

    @ManyToOne(
        optional = false,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "caree_care_giver",
        foreignKey = ForeignKey(name = "fk__contact__caree_care_giver"),
    )
    val careeCareGiver: CareeCareGiver,

): BaseEntity()

enum class PaymentMethod {
    CREDIT_CARD, BANK_TRANSFER, KAKAO_PAY, NAVER_PAY, PAYPAL
}

enum class PaymentStatus {
    PENDING, COMPLETED, CANCELLED, FAILED
}