package com.example.habittracker.data.local

import androidx.room.TypeConverter
import java.time.LocalDate

/**
 * Room stores local calendar dates as ISO-8601 text (`yyyy-MM-dd`).
 *
 * Lexicographic ordering is chronological for this fixed-width representation, so
 * SQLite range queries remain deterministic without timezone or timestamp metadata.
 */
class LocalDateConverters {
    @TypeConverter
    fun fromLocalDate(value: LocalDate): String = value.toString()

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = LocalDate.parse(value)
}
