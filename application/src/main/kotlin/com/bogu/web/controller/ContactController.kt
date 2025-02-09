package com.bogu.web.controller

import com.bogu.service.ContractService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/contracts")
class ContactController(
    private val contractService: ContractService
) {
    @PostMapping("/candidates/{careeId}")
    fun createCandidate(
        @PathVariable careeId: Long,
        @RequestParam(value = "careGiverId", required = true) careGiverId: Long,
    ) {
        require(careeId != careGiverId) {
            "careeId and careGiverId cannot be the same."
        }
        contractService.createCandidate(careeId, careGiverId)
    }

    @DeleteMapping("/candidates/{careeId}")
    fun deleteCandidate(
        @PathVariable careeId: Long,
        @RequestParam(value = "careGiverId", required = true) careGiverId: Long,
    ) {
        require(careeId != careGiverId) {
            "careeId and careGiverId cannot be the same."
        }
        contractService.softDeleteCandidate(careeId, careGiverId)
    }
}