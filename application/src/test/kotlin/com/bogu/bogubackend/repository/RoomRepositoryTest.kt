package com.bogu.bogubackend.repository

import com.bogu.bogubackend.annotation.LocalBootTest
import com.bogu.bogubackend.domain.entity.postgresql.Member
import com.bogu.bogubackend.domain.entity.postgresql.Room
import com.bogu.bogubackend.repo.postgresql.MemberRepository
import com.bogu.bogubackend.repo.postgresql.RoomRepository
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

    "member id 는 left-wing 이 더 작아야 한다" {
        shouldThrow<IllegalArgumentException> {
            val room = Room(
               rightWing, leftWing
            )
            roomRepository.save(room).copy(leftWing = leftWing, rightWing = rightWing)
        }
    }

    "secondary 생성자로 room 생성시 left-wing, right-wing 자동 정렬 됨" {
        Room(rightWing, leftWing).leftWing?.id shouldBe Room(leftWing, rightWing).leftWing?.id
    }

    "동일한 member-id 쌍으로는 room 생성할 수 없음" {
        val room1 = Room(
            leftWing,
            rightWing,
        )

        val room2 = Room(
            rightWing,
            leftWing
        )

        roomRepository.save(room1)
        shouldThrow<DataIntegrityViolationException> {
            roomRepository.saveAndFlush(room2)
        }
    }
}) {
    companion object : KLogging() {
        val member1 = Member(
            memberId = "sj.hong@any.com",
            memberName = "sukjoon",
            nickName = "kkkk",
            password = "a",
        )

        val member2 = Member(
            memberId = "13.hong@any.com",
            memberName = "sukjoon",
            nickName = "kkkk",
            password = "ba",
        )
    }
}