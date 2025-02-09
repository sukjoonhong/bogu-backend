package com.bogu.service

import com.bogu.domain.dto.*
import com.bogu.domain.entity.postgresql.ChatMessage
import com.bogu.domain.entity.postgresql.Member
import com.bogu.domain.entity.postgresql.Profile
import com.bogu.repo.postgresql.ProfileRepository
import com.bogu.service.crud.ChatRoomCrudService
import com.bogu.service.crud.MemberCrudService
import org.springframework.stereotype.Service
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


@Service
class MemberService(
    private val memberCrudService: MemberCrudService,
    private val chatRoomCrudService: ChatRoomCrudService,
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

        val profiles = profileRepository.findProfilesBy(chatRooms.flatMap { it.members.map { m -> m.member.id } })
        val profileByMember = profiles?.associateBy { it.member.id }

        return chatRooms.map { chatRoom ->
            val members = chatRoom.members.map { it.member.toDto(profileByMember?.get(it.member.id)) }
            val chatMessages = chatRoom.chatMessages
                .sortedBy { it.createdAt.toEpochSecond(ZoneOffset.UTC) }
                .map { it.toDto(chatRoom.id) }

            ChatRoomDto(
                id = chatRoom.id,
                lastMessage = chatRoom.chatMessages.maxByOrNull { it.id }?.content ?: "",
                members = members,
                chatMessages = chatMessages
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

    private fun ChatMessage.toDto(roomId: Long): ChatMessageDto {
        return ChatMessageDto(
            type = this.type,
            roomId = roomId,
            senderId = this.sender.id,
            content = this.content,
            createdAt = this.createdAt.format(DateTimeFormatter.ISO_DATE_TIME)
        )
    }
}