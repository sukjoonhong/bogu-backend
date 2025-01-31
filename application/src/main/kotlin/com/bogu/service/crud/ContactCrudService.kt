package com.bogu.service.crud

import com.bogu.domain.entity.postgresql.Contact
import com.bogu.repo.postgresql.ContactRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class ContactCrudService(
    private val contactRepository: ContactRepository,
) {
    fun findAllByCaree(careeId: Long): List<Contact> {
        return contactRepository.findAllByCaree(careeId)
    }

    fun findAllByCareGiver(careGiverId: Long): List<Contact> {
        return contactRepository.findAllByCareGiver(careGiverId)
    }

    companion object: KLogging()
}