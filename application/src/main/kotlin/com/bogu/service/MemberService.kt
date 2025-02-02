package com.bogu.service

import com.bogu.domain.dto.ChatMessageDto
import com.bogu.domain.dto.ChatRoomDto
import com.bogu.domain.dto.MemberDetailsDto
import com.bogu.domain.dto.MemberDto
import com.bogu.domain.entity.postgresql.Member
import com.bogu.domain.entity.postgresql.toDto
import com.bogu.service.crud.MemberCrudService
import org.springframework.stereotype.Service


@Service
class MemberService(
    private val memberCrudService: MemberCrudService
) {
    fun findMemberDetailsDtoByAuthId(authId: String): MemberDetailsDto {
        return memberCrudService.findMemberDetailsByAuthId(authId).toDetailsDto()
    }

    private fun Member.toDetailsDto(): MemberDetailsDto {
        val member = this
        val careGiverCandidates: List<MemberDto> =
            member.getCareGiverCandidates().map { it.toDto() }

        val chatRoomDtos: List<ChatRoomDto> = member.chatRooms.map { chatRoom ->
            require(chatRoom.sender.id == member.id) {
                "invalid request for member.id: ${member.id}, chatRoom.sender.id: ${chatRoom.sender.id}"
            }
            val lastMessage = chatRoom.chatMessages.maxByOrNull { it.id }
            val receiver = chatRoom.receiver

            ChatRoomDto(
                id = chatRoom.id,
                receiverName = receiver.name,
                lastMessage = lastMessage?.content ?: "",
                slaveId = receiver.id,
                masterId = member.id,
                chatMessages = chatRoom.chatMessages.map {
                    ChatMessageDto(
                        type = it.type,
                        roomId = chatRoom.id,
                        senderId = it.sender.id,
                        receiverId = receiver.id,
                        content = it.content,
                        createdAt = it.createdAt
                    )
                }
            )
        }

        return MemberDetailsDto(
            id = member.id,
            careGiverCandidates = careGiverCandidates,
            chatRooms = chatRoomDtos,
        )
    }
}