package com.bogu.util

import java.time.LocalDateTime
import java.time.ZoneOffset

fun utcNow(): LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)

typealias RoomId = Long