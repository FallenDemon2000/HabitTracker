package com.example.habittracker.core.domain.model

data class HabitUpdate(
    val name: String,
    val iconId: HabitIconId,
    val schedule: HabitSchedule,
)
