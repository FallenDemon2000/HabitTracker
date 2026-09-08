package com.example.habittracker.core.domain.model

object HabitIcons {
    val all: Set<HabitIconId> = setOf(
        HabitIconId("run"),
        HabitIconId("read"),
        HabitIconId("water"),
        HabitIconId("meditate"),
        HabitIconId("sleep"),
        HabitIconId("code"),
        HabitIconId("music"),
        HabitIconId("cook"),
        HabitIconId("journal"),
        HabitIconId("gym"),
        HabitIconId("yoga"),
        HabitIconId("walk"),
        HabitIconId("cycle"),
        HabitIconId("study"),
        HabitIconId("no_phone"),
        HabitIconId("vitamins"),
        HabitIconId("language"),
        HabitIconId("gratitude"),
        HabitIconId("health"),
        HabitIconId("organize"),
    )

    fun isKnown(iconId: HabitIconId): Boolean = iconId in all

    val fallback: HabitIconId = HabitIconId("run")
}
