package com.bogu.repository

import com.bogu.annotation.LocalBootTest
import com.bogu.domain.entity.postgresql.Member
import com.bogu.domain.entity.postgresql.ChatRoom
import com.bogu.repo.postgresql.MemberRepository
import com.bogu.repo.postgresql.RoomRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import mu.KLogging
import org.springframework.dao.DataIntegrityViolationException


@LocalBootTest
class RoomRepositoryTest(
    private val roomRepository: RoomRepository,
    private val memberRepository: MemberRepository
) : StringSpec({

    lateinit var rightWing: Member
    lateinit var leftWing: Member

    beforeEach {
        roomRepository.deleteAll()
        memberRepository.deleteAll()
        memberRepository.saveAllAndFlush(listOf(member1, member2))
        rightWing = memberRepository.findById(member1.id).get()
        leftWing = memberRepository.findById(member2.id).get()
    }

    afterSpec {
        roomRepository.deleteAll()
        memberRepository.deleteAll()
    }

    "member id 는 left-wing 이 더 작아야 한다" {
        shouldThrow<IllegalArgumentException> {
            val chatRoom = ChatRoom(
                rightWing, leftWing
            )
            roomRepository.save(chatRoom).deepCopy(leftWing = leftWing, rightWing = rightWing)
        }
    }

    "secondary 생성자로 room 생성시 left-wing, right-wing 자동 정렬 됨" {
        ChatRoom(rightWing, leftWing).leftWing.id shouldBe ChatRoom(leftWing, rightWing).leftWing.id
    }

    "동일한 member-id 쌍으로는 room 생성할 수 없음" {
        val chatRoom1 = ChatRoom(
            leftWing,
            rightWing,
        )

        val chatRoom2 = ChatRoom(
            rightWing,
            leftWing
        )

        roomRepository.save(chatRoom1)
        shouldThrow<DataIntegrityViolationException> {
            roomRepository.saveAndFlush(chatRoom2)
        }
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