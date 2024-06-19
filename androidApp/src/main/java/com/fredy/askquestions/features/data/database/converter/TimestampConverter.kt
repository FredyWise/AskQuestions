package com.fredy.askquestions.features.data.database.converter

import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

object TimestampConverter {
    @JvmStatic
    fun toDateTime(timestamp: Timestamp): LocalDateTime {
        val instant = Instant.ofEpochSecond(
            timestamp.seconds,
            timestamp.nanoseconds.toLong()
        )
        return LocalDateTime.ofInstant(
            instant,
            ZoneOffset.UTC
        )
    }
    @JvmStatic
    fun fromDateTime(dateTime: LocalDateTime): Timestamp {
        return Timestamp(
            dateTime.toEpochSecond(
                ZoneOffset.UTC
            ), dateTime.nano
        )
    }
}
