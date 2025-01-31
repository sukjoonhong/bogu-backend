package com.bogu.repo.postgresql

import com.bogu.domain.entity.postgresql.Contact
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ContactRepository : JpaRepository<Contact, Long> {
    @Query(
        value = """
            SELECT c
            FROM Contact c 
            WHERE c.careeCareGiver.caree = :careeId
        """
    )
    fun findAllByCaree(careeId: Long): List<Contact>

    @Query(
        value = """
            SELECT c
            FROM Contact c 
            WHERE c.careeCareGiver.careGiver = :careGiverId
        """
    )
    fun findAllByCareGiver(careGiverId: Long): List<Contact>
}