package com.bogu.bogubackend.repo.postgresql

import com.bogu.bogubackend.domain.entity.postgresql.Room
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoomRepository: JpaRepository<Room, Long> {
}