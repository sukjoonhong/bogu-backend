package com.bogu.service.crud

import com.bogu.domain.entity.postgresql.Contract
import com.bogu.repo.postgresql.ContractRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class ContractCrudService(
    private val contractRepository: ContractRepository,
) {
    fun findAllByCaree(careeId: Long): List<Contract> {
        return contractRepository.findAllByCaree(careeId)
    }

    fun findAllByCareGiver(careGiverId: Long): List<Contract> {
        return contractRepository.findAllByCareGiver(careGiverId)
    }

    companion object: KLogging()
}