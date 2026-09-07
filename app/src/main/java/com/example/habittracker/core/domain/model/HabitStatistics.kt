package com.example.habittracker.core.domain.model

import java.time.LocalDate

data class TodayHabit(
    val habit: Habit,
    val completed: Boolean,
    val currentStreak: Int,
)

data class TodayProgress(
    val date: LocalDate,
    val habits: List<TodayHabit>,
) {
    val completedCount: Int
        get() = habits.count { it.completed }

    val scheduledCount: Int
        get() = habits.size

    val percentage: Int
        get() = if (scheduledCount == 0) 0 else completedCount * 100 / scheduledCount
}

data class HabitStreak(
    val habitId: HabitId,
    val current: Int,
    val best: Int,
)

data class HeatmapCell(
    val date: LocalDate,
    val completedScheduledHabits: Int,
    val scheduledHabits: Int,
    val percentage: Int?,
) {
    val isFuture: Boolean
        get() = percentage == null
}

data class HabitStatistics(
    val today: TodayProgress,
    val currentWeekPercentage: Int,
    val activeCount: Int,
    val heatmap: List<HeatmapCell>,
    val streaks: List<HabitStreak>,
)
