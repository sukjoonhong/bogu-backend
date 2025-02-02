package com.bogu.service.crud

import com.bogu.repo.postgresql.CareeCareGiverRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CareeCareGiverCrudService(
    private val careeCareGiverRepository: CareeCareGiverRepository
) {
    @Transactional
    fun create(careeId: Long, careGiverId: Long) {
        careeCareGiverRepository.insertIfNotExists(careeId, careGiverId)
    }

    @Transactional
    fun softDeleteBy(careeId: Long, careGiverId: Long) {
        careeCareGiverRepository.softDeleteBy(careeId, careGiverId)
    }
}