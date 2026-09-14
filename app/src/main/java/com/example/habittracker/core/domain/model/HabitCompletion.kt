package com.example.habittracker.core.domain.model

import java.time.ZonedDateTime

data class HabitCompletion(
    val habitId: HabitId,
    val date: ZonedDateTime,
)
