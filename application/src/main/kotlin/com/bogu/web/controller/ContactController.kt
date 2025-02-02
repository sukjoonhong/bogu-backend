package com.bogu.web.controller

import com.bogu.service.ContactService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/contact")
class ContactController(
    private val contactService: ContactService
) {
    @PostMapping("/candidates/{careeId}")
    fun createCandidate(
        @PathVariable careeId: Long,
        @RequestParam(value = "careGiverId", required = true) careGiverId: Long,
    ) {
        require(careeId != careGiverId) {
            "careeId and careGiverId cannot be the same."
        }
        contactService.createCandidate(careeId, careGiverId)
    }

    @DeleteMapping("/candidates/{careeId}")
    fun deleteCandidate(
        @PathVariable careeId: Long,
        @RequestParam(value = "careGiverId", required = true) careGiverId: Long,
    ) {
        require(careeId != careGiverId) {
            "careeId and careGiverId cannot be the same."
        }
        contactService.softDeleteCandidate(careeId, careGiverId)
    }
}