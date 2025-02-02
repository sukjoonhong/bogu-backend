package com.bogu.repository

import com.bogu.annotation.LocalBootTest
import com.bogu.domain.entity.postgresql.Member
import com.bogu.repo.postgresql.ChatRoomRepository
import com.bogu.repo.postgresql.MemberRepository
import io.kotest.core.spec.style.StringSpec
import mu.KLogging


@LocalBootTest
class ChatRoomRepositoryTest(
    private val chatRoomRepository: ChatRoomRepository,
    private val memberRepository: MemberRepository
) : StringSpec({

}) {
    companion object : KLogging() {
        val member1 = Member(
            authId = "sj.hong@any.com",
            name = "sukjoon",
            nickName = "kkkk",
            password = "a",
        )

        val member2 = Member(
            authId = "13.hong@any.com",
            name = "sukjoon",
            nickName = "kkkk",
            password = "ba",
        )
    }
}