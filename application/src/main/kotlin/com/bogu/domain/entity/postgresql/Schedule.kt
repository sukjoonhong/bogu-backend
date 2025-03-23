package com.bogu.domain.entity.postgresql

import com.bogu.domain.ScheduleStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "schedule")
data class Schedule(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val title: String,

    @Column(columnDefinition = "TEXT")
    val description: String? = null,

    @Column(name = "start_time", nullable = false)
    val startTime: LocalDateTime,

    @Column(name = "end_time", nullable = false)
    val endTime: LocalDateTime,

    @Column
    val location: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: ScheduleStatus = ScheduleStatus.SCHEDULED,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "caree_care_giver_id",
        foreignKey = ForeignKey(name = "fk__schedule__caree_care_giver")
    )
    val careeCareGiver: CareeCareGiver,

): BaseEntity()