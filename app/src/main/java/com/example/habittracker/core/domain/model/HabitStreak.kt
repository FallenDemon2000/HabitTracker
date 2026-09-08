package com.example.habittracker.core.domain.model

data class HabitStreak(
    val habitId: HabitId,
    val current: Int,
    val best: Int,
)
