package com.bogu.service.crud

import com.bogu.domain.CareeCareGiverStatus
import com.bogu.repo.postgresql.CareeCareGiverRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CareeCareGiverCrudService(
    private val careeCareGiverRepository: CareeCareGiverRepository
) {
    @Transactional
    fun createRequestMatching(careeId: Long, careGiverId: Long) {
        careeCareGiverRepository.createOrReactivateRequestMatching(careeId, careGiverId)
    }

    @Transactional
    fun cancelRequestMatching(careeId: Long, careGiverId: Long) {
        careeCareGiverRepository.updateStatusBy(
            careeId,
            careGiverId,
            CareeCareGiverStatus.CANCELED.name
        )
    }

    @Transactional
    fun createMatching(careeId: Long, careGiverId: Long) {
        careeCareGiverRepository.updateStatusBy(
            careeId,
            careGiverId,
            CareeCareGiverStatus.MATCHED.name
        )
    }
}