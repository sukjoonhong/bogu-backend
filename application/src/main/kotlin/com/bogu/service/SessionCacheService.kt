package com.bogu.service

import com.bogu.util.RoomId
import jakarta.annotation.PostConstruct
import mu.KLogging
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession

@Service
class SessionCacheService {
    private val roomSessionsMap = mutableMapOf<RoomId, MutableSet<WebSocketSession>>()
    private val sessionIdToRoomMap = mutableMapOf<String, RoomId>()

    @PostConstruct
    fun init() {
        clear()
    }

    fun getSessions(roomId: Long): Set<WebSocketSession>? {
        return roomSessionsMap[roomId]
    }

    fun add(roomId: Long, session: WebSocketSession) {
        roomSessionsMap.getOrPut(roomId) { mutableSetOf() }.add(session)
        sessionIdToRoomMap[session.id] = roomId
    }

    fun remove(roomId: Long, session: WebSocketSession) {
        roomSessionsMap[roomId]?.remove(session)
        sessionIdToRoomMap.remove(session.id)
    }

    fun remove(session: WebSocketSession) {
        val roomId = sessionIdToRoomMap[session.id]
        roomSessionsMap[roomId]?.remove(session)
        sessionIdToRoomMap.remove(session.id)
    }

    fun clear() {
        roomSessionsMap.clear()
        sessionIdToRoomMap.clear()
    }

    companion object : KLogging()
}