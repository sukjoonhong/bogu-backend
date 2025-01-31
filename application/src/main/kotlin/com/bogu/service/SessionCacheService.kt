package com.bogu.service

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

    @PostConstruct
    fun init() {
        clear()
    }

    fun getSessions(roomId: Long): Set<WebSocketSession>? {
        return roomSessionsMap[roomId]
    }

    fun add(roomId: Long, session: WebSocketSession) {
        roomSessionsMap.computeIfAbsent(roomId) { ConcurrentHashMap.newKeySet() }.add(session)
        sessionRoomMap[session.id] = roomId
    }

    fun remove(session: WebSocketSession) {
        val roomId = sessionRoomMap[session.id]
        roomSessionsMap[roomId]?.remove(session)
        sessionRoomMap.remove(session.id)
    }

    fun clear() {
        roomSessionsMap.clear()
        sessionRoomMap.clear()
    }

    companion object : KLogging()
}