package com.bogu.repo.postgresql

import com.bogu.domain.entity.postgresql.Hospital
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HospitalRepository: JpaRepository<Hospital, Long> {
}