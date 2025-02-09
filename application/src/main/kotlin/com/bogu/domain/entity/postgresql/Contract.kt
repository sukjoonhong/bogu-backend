package com.bogu.domain.entity.postgresql

import com.bogu.domain.PaymentMethod
import com.bogu.domain.PaymentStatus
import jakarta.persistence.*

@Entity
@Table(
    name = "contract",
)
data class Contract(
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
        foreignKey = ForeignKey(name = "fk__contract__caree_care_giver"),
    )
    val careeCareGiver: CareeCareGiver
): BaseEntity()