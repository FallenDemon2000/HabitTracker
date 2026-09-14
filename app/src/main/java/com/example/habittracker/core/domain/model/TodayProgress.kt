package com.example.habittracker.core.domain.model

import java.time.ZonedDateTime

data class TodayProgress(
    val date: ZonedDateTime,
    val habits: List<TodayHabit>,
) {
    val completedCount: Int
        get() = habits.count { it.completed }

    val scheduledCount: Int
        get() = habits.size

    val percentage: Int
        get() = if (scheduledCount == 0) 0 else completedCount * 100 / scheduledCount
}
