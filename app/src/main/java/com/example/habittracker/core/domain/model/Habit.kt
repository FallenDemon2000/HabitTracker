package com.example.habittracker.core.domain.model

import java.time.DayOfWeek
import java.time.LocalDate

@JvmInline
value class HabitId(val value: Long) {
    init {
        require(value > 0) { "Habit IDs must be positive." }
    }
}

@JvmInline
value class HabitIconId(val value: String) {
    init {
        require(value.isNotBlank()) { "Habit icon IDs must not be blank." }
    }
}

object HabitIcons {
    val all: Set<HabitIconId> = setOf(
        HabitIconId("run"),
        HabitIconId("read"),
        HabitIconId("water"),
        HabitIconId("meditate"),
        HabitIconId("sleep"),
        HabitIconId("code"),
        HabitIconId("music"),
        HabitIconId("cook"),
        HabitIconId("journal"),
        HabitIconId("gym"),
        HabitIconId("yoga"),
        HabitIconId("walk"),
        HabitIconId("cycle"),
        HabitIconId("study"),
        HabitIconId("no_phone"),
        HabitIconId("vitamins"),
        HabitIconId("language"),
        HabitIconId("gratitude"),
        HabitIconId("health"),
        HabitIconId("organize"),
    )

    fun isKnown(iconId: HabitIconId): Boolean = iconId in all

    val fallback: HabitIconId = HabitIconId("run")
}

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

data class Habit(
    val id: HabitId,
    val name: String,
    val iconId: HabitIconId,
    val weekdayMask: WeekdayMask,
    val creationDate: LocalDate,
)

data class HabitDraft(
    val name: String,
    val iconId: HabitIconId,
    val weekdayMask: WeekdayMask,
    val creationDate: LocalDate,
)

data class HabitUpdate(
    val name: String,
    val iconId: HabitIconId,
    val weekdayMask: WeekdayMask,
)

data class HabitCompletion(
    val habitId: HabitId,
    val date: LocalDate,
)
