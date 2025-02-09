package com.bogu.domain

import com.fasterxml.jackson.annotation.JsonValue

enum class ChatMessageType(@JsonValue val jsonValue: String) {
    JOIN("join"), CHAT("chat"), LEAVE("leave"),
    ROOM_CREATE("room_create"), ROOM_UPDATE("room_update");

    fun isChatMessage(): Boolean {
        return this == JOIN || this == CHAT || this == LEAVE
    }
}