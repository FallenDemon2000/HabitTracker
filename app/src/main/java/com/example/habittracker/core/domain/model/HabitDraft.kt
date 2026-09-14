package com.example.habittracker.core.domain.model

import java.time.ZonedDateTime

data class HabitDraft(
    val name: String,
    val iconId: HabitIconId,
    val schedule: HabitSchedule,
    val creationDate: ZonedDateTime,
)
