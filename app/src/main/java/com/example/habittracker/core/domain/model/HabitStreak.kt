package com.example.habittracker.core.domain.model

data class HabitStreak(
    val habitId: HabitId,
    val name: String,
    val icon: HabitIcon,
    val current: Int,
    val best: Int,
)
