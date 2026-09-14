package com.example.habittracker.core.domain.model

import java.time.ZonedDateTime

data class HeatmapCell(
    val date: ZonedDateTime,
    val completedScheduledHabits: Int,
    val scheduledHabits: Int,
    val percentage: Int?,
) {
    val isFuture: Boolean
        get() = percentage == null
}
