package com.example.habittracker.core.domain.model

import java.time.ZonedDateTime

data class Habit(
    val id: HabitId,
    val name: String,
    val icon: HabitIcon,
    val schedule: HabitSchedule,
    val creationDate: ZonedDateTime,
)
