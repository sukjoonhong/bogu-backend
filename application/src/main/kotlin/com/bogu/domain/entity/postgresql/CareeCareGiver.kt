package com.bogu.domain.entity.postgresql

import jakarta.persistence.*

@Entity
@Table(
    name = "caree_care_giver",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq__caree_care_giver__caree_care_giver",
            columnNames = ["caree", "care_giver"]
        )
    ],
    indexes = [
        Index(name = "idx__caree_care_giver__caree", columnList = "caree"),
        Index(name = "idx__caree_care_giver__care_giver", columnList = "care_giver")
    ]
)
data class CareeCareGiver(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @ManyToOne(
        optional = false,
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "caree",
        foreignKey = ForeignKey(name = "fk__caree_care_giver__caree"),
    )
    val caree: Member,

    @ManyToOne(
        optional = false,
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "care_giver",
        foreignKey = ForeignKey(name = "fk__caree_care_giver__care_giver"),
    )
    val careGiver: Member,
) : BaseEntity()