package com.example.habittracker.core.domain.model

import java.time.LocalDate

data class HabitCompletion(
    val habitId: HabitId,
    val date: LocalDate,
)
