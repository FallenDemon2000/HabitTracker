package com.example.habittracker.core.domain.model

import java.time.LocalDate

data class HeatmapCell(
    val date: LocalDate,
    val completedScheduledHabits: Int,
    val scheduledHabits: Int,
    val percentage: Int?,
) {
    val isFuture: Boolean
        get() = percentage == null
}
