package com.bogu.bogubackend.repository

import com.bogu.bogubackend.annotation.LocalBootTest
import com.bogu.bogubackend.domain.entity.postgresql.Member
import com.bogu.bogubackend.repo.postgresql.MemberRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.delay
import mu.KLogging

@LocalBootTest
class BaseEntityTest(
    private val memberRepository: MemberRepository
) : StringSpec({
    beforeSpec {
        memberRepository.deleteByMemberId("test_member_id")
    }

    afterSpec {
        memberRepository.deleteByMemberId("test_member_id")
    }

    "createdAt, modifiedAt test" {
        val member = Member(
            memberId = "test_member_id",
            memberName = "sukjoon",
            nickName = "kkkk",
            password = "a",
        )

        memberRepository.save(member)

        val createdAt = memberRepository.findByMemberId("test_member_id")!!.createdAt

        createdAt shouldNotBe null

        delay(TEST_PERIOD)

        memberRepository.save(member.copy(memberName = "sukjoon2"))

        val modifiedAt = memberRepository.findByMemberId("test_member_id")!!.modifiedAt

        modifiedAt shouldNotBe null
        modifiedAt.second shouldBeGreaterThanOrEqual (createdAt.second + TEST_PERIOD.div(1000L).toInt())
    }
}) {
    companion object : KLogging() {
        const val TEST_PERIOD = 3000L
    }
}