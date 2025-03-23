package com.bogu.domain.entity.postgresql

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "hospital")
data class Hospital(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "encrypted_code", nullable = false, unique = true)
    val encryptedCode: String,

    @Column(nullable = false)
    val name: String,

    @Column(name = "type_code", nullable = false)
    val typeCode: String,

    @Column(name = "type_name", nullable = false)
    val typeName: String,

    @Column(name = "sido_code", nullable = false)
    val sidoCode: String,

    @Column(name = "sido_name", nullable = false)
    val sidoName: String,

    @Column(name = "sigungu_code", nullable = false)
    val sigunguCode: String,

    @Column(name = "sigungu_name", nullable = false)
    val sigunguName: String,

    @Column
    val eupmyeondong: String? = null,

    @Column(name = "postal_code")
    val postalCode: String? = null,

    @Column(nullable = false)
    val address: String,

    @Column
    val phone: String? = null,

    @Column
    val homepage: String? = null,

    @Column(name = "established_at")
    val establishedAt: LocalDate? = null,

    @Column
    val longitude: Double? = null,

    @Column
    val latitude: Double? = null,
): BaseEntity()

