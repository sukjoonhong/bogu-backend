package com.bogu.service.chat

import com.bogu.domain.dto.ChatMessageDto
import com.bogu.util.MemberId
import com.bogu.util.RoomId
import com.bogu.util.SessionId
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogging
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap


@Service
class ChatRoomSessionService(
    private val objectMapper: ObjectMapper
) {
    private val roomIdToMembers = ConcurrentHashMap<RoomId, MutableSet<MemberId>>()

    private val memberIdToSessionMap = ConcurrentHashMap<MemberId, WebSocketSession>()
    private val sessionIdToMemberIdMap = ConcurrentHashMap<SessionId, MemberId>()

    /**
     * 세션 연결 시 (로그인 + WebSocket 연결)
     */
    fun add(session: WebSocketSession, memberId: Long) {
        memberIdToSessionMap[memberId] = session
        sessionIdToMemberIdMap[session.id] = memberId
    }

    /**
     * 세션 해제 시 (WebSocket 종료)
     */
    fun remove(session: WebSocketSession) {
        val memberId = sessionIdToMemberIdMap.remove(session.id) ?: return
        memberIdToSessionMap.remove(memberId)

        roomIdToMembers.forEach { (_, members) ->
            members.remove(memberId)
        }
    }

    /**
     * 방에 멤버 추가 (JOIN)
     */
    fun addMemberToRoom(roomId: Long, memberId: Long) {
        roomIdToMembers.computeIfAbsent(roomId) { ConcurrentHashMap.newKeySet() }
            .add(memberId)
    }

    /**
     * 특정 방에 속한 모든 멤버 ID 조회
     */
    fun getMemberIdsInRoom(roomId: Long): Set<Long> {
        return roomIdToMembers[roomId] ?: emptySet()
    }

    fun getSessionBy(memberId: Long): WebSocketSession? {
        return memberIdToSessionMap[memberId]
    }


    fun toTextMessage(chatMessage: ChatMessageDto): TextMessage {
        return TextMessage(objectMapper.writeValueAsString(chatMessage))
    }

    /**
     * 데이터 불일치 해소를 위해 sync 하는 함수
     */
    fun sync() {
        memberIdToSessionMap.forEach { (memberId, session) ->
            if (!session.isOpen) {
                removeSessionData(memberId)
                return@forEach
            }

            val foundMemberId = sessionIdToMemberIdMap[session.id]
            if (foundMemberId == null || foundMemberId != memberId) {
                removeSessionData(memberId)
            }
        }

        sessionIdToMemberIdMap.forEach { (sessionId, memberId) ->
            val session = memberIdToSessionMap[memberId]
            if (session == null || session.id != sessionId || !session.isOpen) {
                removeSessionData(memberId)
            }
        }

        roomIdToMembers.forEach { (_, memberSet) ->
            val iterator = memberSet.iterator()
            while (iterator.hasNext()) {
                val memberId = iterator.next()
                val session = memberIdToSessionMap[memberId]
                if (session == null || !session.isOpen) {
                    iterator.remove()
                }
            }
        }
    }

    /**
     * memberId 관련 데이터 삭제 (세션, 매핑, room 정보 등)
     */
    private fun removeSessionData(memberId: MemberId) {
        val session = memberIdToSessionMap.remove(memberId)

        if (session != null) {
            sessionIdToMemberIdMap.remove(session.id)
        }

        roomIdToMembers.forEach { (_, members) ->
            members.remove(memberId)
        }
    }

    companion object : KLogging()
}