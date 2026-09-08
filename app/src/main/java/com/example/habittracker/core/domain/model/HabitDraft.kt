package com.example.habittracker.core.domain.model

import java.time.LocalDate

data class HabitDraft(
    val name: String,
    val iconId: HabitIconId,
    val weekdayMask: WeekdayMask,
    val creationDate: LocalDate,
)
