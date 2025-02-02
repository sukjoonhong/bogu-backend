package com.bogu.repository

import com.bogu.annotation.LocalBootTest
import com.bogu.domain.entity.postgresql.Member
import com.bogu.domain.entity.postgresql.ChatRoom
import com.bogu.repo.postgresql.MemberRepository
import com.bogu.repo.postgresql.ChatRoomRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import mu.KLogging
import org.springframework.dao.DataIntegrityViolationException


@LocalBootTest
class ChatRoomRepositoryTest(
    private val chatRoomRepository: ChatRoomRepository,
    private val memberRepository: MemberRepository
) : StringSpec({

    lateinit var rightWing: Member
    lateinit var leftWing: Member

    beforeEach {
        chatRoomRepository.deleteAll()
        memberRepository.deleteAll()
        memberRepository.saveAllAndFlush(listOf(member1, member2))
        rightWing = memberRepository.findById(member1.id).get()
        leftWing = memberRepository.findById(member2.id).get()
    }

    afterSpec {
        chatRoomRepository.deleteAll()
        memberRepository.deleteAll()
    }

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