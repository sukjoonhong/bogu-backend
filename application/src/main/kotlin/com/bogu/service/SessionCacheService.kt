package com.bogu.service

import com.bogu.util.MemberId
import com.bogu.util.RoomId
import com.bogu.util.SessionId
import jakarta.annotation.PostConstruct
import mu.KLogging
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Service
class SessionCacheService {
    private val roomSessionsMap = ConcurrentHashMap<RoomId, MutableSet<WebSocketSession>>()
    private val sessionRoomMap = ConcurrentHashMap<SessionId, RoomId>()
    private val memberSessionMap = ConcurrentHashMap<SessionId, MemberId>()

    @PostConstruct
    fun init() {
        clear()
    }

    fun getSessions(roomId: Long): Set<WebSocketSession>? {
        return roomSessionsMap[roomId]
    }

    fun getMemberSession(sessionId: String): MemberId? {
        return memberSessionMap[sessionId]
    }

    fun add(roomId: Long, senderId: Long, session: WebSocketSession) {
        roomSessionsMap.computeIfAbsent(roomId) { ConcurrentHashMap.newKeySet() }.add(session)
        sessionRoomMap[session.id] = roomId
        memberSessionMap[session.id] = senderId
    }

    fun remove(session: WebSocketSession) {
        val roomId = sessionRoomMap[session.id]
        roomSessionsMap[roomId]?.remove(session)
        sessionRoomMap.remove(session.id)
        memberSessionMap.remove(session.id)
    }

    fun clear() {
        roomSessionsMap.clear()
        sessionRoomMap.clear()
    }

    companion object : KLogging()
}