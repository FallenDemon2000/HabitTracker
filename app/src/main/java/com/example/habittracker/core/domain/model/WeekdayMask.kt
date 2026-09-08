package com.example.habittracker.core.domain.model

import java.time.DayOfWeek
import java.time.LocalDate

/**
 * A seven-bit schedule. Sunday is bit 0, Monday bit 1, through Saturday bit 6.
 */
@JvmInline
value class WeekdayMask(val value: Int) {
    init {
        require(value in 1..127) { "A weekday mask must contain at least one day." }
    }

    fun isScheduled(date: LocalDate): Boolean {
        return value and date.weekdayBit() != 0
    }
}

/** Centralized Sunday-bit-0 mapping used by persistence and date calculations. */
fun LocalDate.weekdayBit(): Int = 1 shl (dayOfWeek.value % 7)

fun weekdayMaskFor(days: Set<DayOfWeek>): WeekdayMask {
    return WeekdayMask(days.fold(0) { mask, day -> mask or (1 shl (day.value % 7)) })
}
