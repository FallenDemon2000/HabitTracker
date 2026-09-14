package com.example.habittracker.data.local

import com.example.habittracker.core.domain.model.Habit
import com.example.habittracker.core.domain.model.HabitCompletion
import com.example.habittracker.core.domain.model.HabitDraft
import com.example.habittracker.core.domain.model.HabitIcon
import com.example.habittracker.core.domain.model.HabitId
import com.example.habittracker.core.domain.model.HabitIcons
import com.example.habittracker.core.domain.model.HabitSchedule

fun HabitEntity.toDomain(): Habit {
    val storedIcon = icon.toHabitIconOrNull()
    return Habit(
        id = HabitId(id),
        name = name,
        icon = if (storedIcon != null && HabitIcons.isKnown(storedIcon)) {
            storedIcon
        } else {
            HabitIcons.fallback
        },
        schedule = HabitSchedule.fromBitMask(weekdayMask),
        creationDate = creationDate,
    )
}

fun HabitCompletionEntity.toDomain(): HabitCompletion = HabitCompletion(
    habitId = HabitId(habitId),
    date = date,
)

fun HabitDraft.toEntity(): HabitEntity = HabitEntity(
    name = name,
    icon = icon.name,
    weekdayMask = schedule.toBitMask(),
    creationDate = creationDate,
)

fun Habit.toEntity(): HabitEntity = HabitEntity(
    id = id.value,
    name = name,
    icon = icon.name,
    weekdayMask = schedule.toBitMask(),
    creationDate = creationDate,
)

fun List<HabitEntity>.toDomain(): List<Habit> = map(HabitEntity::toDomain)

fun List<HabitCompletionEntity>.toCompletionDomain(): List<HabitCompletion> =
    map(HabitCompletionEntity::toDomain)

private fun String.toHabitIconOrNull(): HabitIcon? {
    return runCatching { HabitIcon.valueOf(this) }
        .getOrNull()
        ?: HabitIcon.entries.firstOrNull { it.name.equals(this, ignoreCase = true) }
}
