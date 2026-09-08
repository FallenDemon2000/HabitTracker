package com.example.habittracker.core.domain.model

data class TodayHabit(
    val habit: Habit,
    val completed: Boolean,
    val currentStreak: Int,
)
