package com.bogu.util

import java.time.LocalDateTime
import java.time.ZoneOffset

fun utcNow(): LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)

enum class TimeUnit(private val millis: Long, private val seconds: Long) {
    SECOND(1_000L, 1L),
    MINUTE(60_000L, 60L),
    HOUR(3_600_000L, 3_600L),
    DAY(86_400_000L, 86_400L);

    fun toMillis(value: Long): Long = value * millis
    fun toSeconds(value: Long): Long = value * seconds
}

typealias RoomId = Long
typealias SessionId = String
typealias MemberId = Long