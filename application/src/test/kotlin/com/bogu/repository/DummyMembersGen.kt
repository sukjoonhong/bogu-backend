package com.bogu.repository

import com.bogu.annotation.LocalBootTest
import com.bogu.domain.entity.postgresql.Member
import com.bogu.repo.postgresql.MemberRepository
import io.kotest.core.spec.style.StringSpec

@LocalBootTest
class DummyMembersGen(
    private val memberRepository: MemberRepository
) : StringSpec({

    "test 용 더미 데이터 생성" {
        val members = mutableListOf<Member>()
        for (i in 1..20) {
            members.add(
                Member(
                    authId = "dummyId$i",
                    name = "dummyName$i",
                    nickName = "dummyNickName$i",
                    password = "dummyPassword$i",
                )
            )
        }
        memberRepository.saveAllAndFlush(members)
    }
})