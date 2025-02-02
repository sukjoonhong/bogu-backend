package com.bogu.domain.entity.postgresql

import com.bogu.util.utcNow
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = utcNow()

    @Column(name = "modified_at", nullable = false)
    @LastModifiedDate
    var modifiedAt: LocalDateTime = utcNow()
}