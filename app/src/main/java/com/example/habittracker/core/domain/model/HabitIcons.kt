package com.example.habittracker.core.domain.model

object HabitIcons {
    val all: Set<HabitIcon> = HabitIcon.entries.toSet()

    fun isKnown(icon: HabitIcon): Boolean = icon in all

    val fallback: HabitIcon = HabitIcon.RUN
}
