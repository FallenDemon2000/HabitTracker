package com.example.habittracker.core.domain.model

data class HabitUpdate(
    val name: String,
    val icon: HabitIcon,
    val schedule: HabitSchedule,
)
