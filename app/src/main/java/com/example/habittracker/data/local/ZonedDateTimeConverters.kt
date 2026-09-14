package com.example.habittracker.data.local

import androidx.room.TypeConverter
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Epoch milliseconds are used only at the Room serialization boundary.
 * Domain and repository APIs retain the original zone and date-time semantics.
 */
class ZonedDateTimeConverters {
    @TypeConverter
    fun fromZonedDateTime(value: ZonedDateTime): Long = value.toInstant().toEpochMilli()

    @TypeConverter
    fun toZonedDateTime(value: Long): ZonedDateTime =
        Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault())
}
