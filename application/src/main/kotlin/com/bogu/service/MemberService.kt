package com.bogu.service

import com.bogu.domain.dto.ChatMessageDto.Companion.DEFAULT_FETCH_SIZE
import com.bogu.domain.dto.ChatMessageDto.Companion.FETCH_SIZE
import com.bogu.domain.dto.ChatRoomDto
import com.bogu.domain.dto.MemberDetailsDto
import com.bogu.domain.dto.MemberDto
import com.bogu.domain.dto.ProfileDto
import com.bogu.domain.entity.postgresql.Member
import com.bogu.domain.entity.postgresql.Profile
import com.bogu.domain.entity.postgresql.toDto
import com.bogu.repo.postgresql.ProfileRepository
import com.bogu.service.crud.ChatMessageCrudService
import com.bogu.service.crud.ChatRoomCrudService
import com.bogu.service.crud.MemberCrudService
import org.springframework.stereotype.Service
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


@Service
class MemberService(
    private val memberCrudService: MemberCrudService,
    private val chatRoomCrudService: ChatRoomCrudService,
    private val chatMessageCrudService: ChatMessageCrudService,
    private val profileRepository: ProfileRepository
) {
    fun loadInitialMemberInfos(authId: String): MemberDetailsDto {
        val member = memberCrudService.findByAuthId(authId)
        val careGiverCandidates = fetchCareGivers(member)
        val chatRooms = fetchChatRooms(member.id)

        return toMemberDetailsDto(member, chatRooms, careGiverCandidates)
    }

    private fun fetchCareGivers(member: Member): List<MemberDto> {
        val candidates = memberCrudService.findCareGiverCandidates(member.id) ?: return listOf()
        val candidateProfiles = profileRepository.findProfilesBy(candidates.map { it.id })

        val profileByMember = candidateProfiles?.associateBy { it.member.id }
        return candidates.map { it.toDto(profileByMember?.get(it.id)) }
    }

    private fun fetchChatRooms(memberId: Long): List<ChatRoomDto> {
        val chatRoomIds = chatRoomCrudService.findChatRoomIdsBy(memberId)
        val chatRooms = chatRoomCrudService.findChatRoomsBy(chatRoomIds, memberId)
        val chatMessageMap = chatMessageCrudService.findLatest3MessagesByChatRoom(chatRoomIds).groupBy { it.chatRoom.id }

        val profiles = profileRepository.findProfilesBy(chatRooms.flatMap { it.members.map { m -> m.member.id } })
        val profileByMember = profiles?.associateBy { it.member.id }

        return chatRooms.map { chatRoom ->
            val members = chatRoom.members.map { it.member.toDto(profileByMember?.get(it.member.id)) }

            val chatMessages = chatMessageMap.getOrDefault(chatRoom.id, emptyList())
            val chatMessageDtos = chatMessages
                .sortedBy { it.createdAt.toEpochSecond(ZoneOffset.UTC) }
                .map { it.toDto(chatRoom.id) }

            ChatRoomDto(
                id = chatRoom.id,
                lastMessage = chatMessageDtos.lastOrNull()?.content ?: "",
                members = members,
                chatMessages = chatMessageDtos.filter { it.type.isChatMessage() },
                lastMessageSentAt = chatMessages
                    .maxByOrNull { it.id }
                    ?.createdAt?.format(DateTimeFormatter.ISO_DATE_TIME) ?: "",

                offset = chatMessageDtos.size,
                limit = FETCH_SIZE,
                hasNext = chatMessages.size >= DEFAULT_FETCH_SIZE
            )
        }
    }


    private fun toMemberDetailsDto(
        member: Member,
        chatRooms: List<ChatRoomDto>,
        careGiverCandidates: List<MemberDto>
    ): MemberDetailsDto {
        return MemberDetailsDto(
            id = member.id,
            careGiverCandidates = careGiverCandidates,
            chatRooms = chatRooms
        )
    }

    private fun Member.toDto(profile: Profile?): MemberDto {
        return MemberDto(
            id = this.id,
            name = this.name,
            nickName = this.nickName,
            profile = profile?.let { ProfileDto(id = it.id) }
        )
    }
}