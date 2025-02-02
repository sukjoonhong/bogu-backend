package com.bogu.repository

import com.bogu.annotation.LocalBootTest
import com.bogu.repo.postgresql.MemberRepository
import io.kotest.core.spec.style.StringSpec
import mu.KLogging

@LocalBootTest
class BaseEntityTest(
    private val memberRepository: MemberRepository
) : StringSpec({

}) {
    companion object : KLogging() {
        const val TEST_PERIOD = 3000L
    }
}