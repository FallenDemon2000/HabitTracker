package com.example.habittracker.core.domain.model

data class HabitStatistics(
    val today: TodayProgress,
    val currentWeekPercentage: Int,
    val activeCount: Int,
    val heatmap: List<HeatmapCell>,
    val streaks: List<HabitStreak>,
)
