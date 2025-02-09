package com.bogu.domain

import com.fasterxml.jackson.annotation.JsonValue

enum class ChatRoomType(@JsonValue val jsonValue: String) {
    DIRECT("direct"), GROUP("group")
}