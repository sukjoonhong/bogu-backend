package com.bogu.repo.postgresql

import com.bogu.domain.entity.postgresql.Contract
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ContractRepository : JpaRepository<Contract, Long> {
    @Query(
        value = """
            SELECT c
            FROM Contract c 
            WHERE c.careeCareGiver.caree.id = :careeId
        """
    )
    fun findAllByCaree(careeId: Long): List<Contract>

    @Query(
        value = """
            SELECT c
            FROM Contract c 
            WHERE c.careeCareGiver.careGiver.id = :careGiverId
        """
    )
    fun findAllByCareGiver(careGiverId: Long): List<Contract>
}