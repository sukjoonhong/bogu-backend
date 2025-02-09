package com.bogu.service

import com.bogu.service.crud.CareeCareGiverCrudService
import com.bogu.service.crud.ContractCrudService
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class ContractService(
    private val contractCrudService: ContractCrudService,
    private val careeCareGiverCrudService: CareeCareGiverCrudService
) {
    fun createCandidate(careeId: Long, careGiverId: Long) {
        careeCareGiverCrudService.create(careeId, careGiverId)
        logger.info { "creating candidate for caree $careeId, caregiver $careGiverId" }
    }

    fun softDeleteCandidate(careeId: Long, careGiverId: Long) {
        careeCareGiverCrudService.softDeleteBy(careeId, careGiverId)
    }

    companion object: KLogging()
}