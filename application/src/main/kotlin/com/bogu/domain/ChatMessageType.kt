package com.bogu.domain

import com.fasterxml.jackson.annotation.JsonValue

/**
 * @property ROOM_CREATE client 에서 로그인시 채팅방 정렬을 위해 필요한 더미 메시지용 enum 값
 * @property ROOM_UPDATE client 에서 로그인시 채팅방 정렬을 위해 필요한 더미 메시지용 enum 값
 * @param jsonValue flutter enum 이 소문자이기 때문에 정의해놓음
 */
enum class ChatMessageType(@JsonValue val jsonValue: String) {
    JOIN("join"), CHAT("chat"), LEAVE("leave"),
    ROOM_CREATE("room_create"), ROOM_UPDATE("room_update");

    fun isChatMessage(): Boolean {
        return this == JOIN || this == CHAT || this == LEAVE
    }
}