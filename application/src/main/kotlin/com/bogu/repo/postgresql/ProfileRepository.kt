package com.bogu.repo.postgresql

import com.bogu.domain.entity.postgresql.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfileRepository: JpaRepository<Profile, Long> {
}