package com.example.habittracker.core.domain.model

import java.time.DayOfWeek
import java.time.ZonedDateTime

data class HabitSchedule(
    val monday: Boolean = false,
    val tuesday: Boolean = false,
    val wednesday: Boolean = false,
    val thursday: Boolean = false,
    val friday: Boolean = false,
    val saturday: Boolean = false,
    val sunday: Boolean = false,
) {
    init {
        require(monday || tuesday || wednesday || thursday || friday || saturday || sunday) {
            "A habit schedule must contain at least one day."
        }
    }

    fun isScheduled(date: ZonedDateTime): Boolean {
        return when (date.dayOfWeek) {
            DayOfWeek.MONDAY -> monday
            DayOfWeek.TUESDAY -> tuesday
            DayOfWeek.WEDNESDAY -> wednesday
            DayOfWeek.THURSDAY -> thursday
            DayOfWeek.FRIDAY -> friday
            DayOfWeek.SATURDAY -> saturday
            DayOfWeek.SUNDAY -> sunday
        }
    }

    fun toBitMask(): Int {
        return (if (sunday) 1 else 0) or
            (if (monday) 2 else 0) or
            (if (tuesday) 4 else 0) or
            (if (wednesday) 8 else 0) or
            (if (thursday) 16 else 0) or
            (if (friday) 32 else 0) or
            (if (saturday) 64 else 0)
    }

    companion object {
        fun fromBitMask(mask: Int): HabitSchedule {
            require(mask in 1..127) { "A weekday mask must contain at least one day." }
            return HabitSchedule(
                monday = mask and 2 != 0,
                tuesday = mask and 4 != 0,
                wednesday = mask and 8 != 0,
                thursday = mask and 16 != 0,
                friday = mask and 32 != 0,
                saturday = mask and 64 != 0,
                sunday = mask and 1 != 0,
            )
        }
    }
}
