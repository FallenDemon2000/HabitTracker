package com.example.habittracker.data.local

import com.example.habittracker.core.domain.model.Habit
import com.example.habittracker.core.domain.model.HabitCompletion
import com.example.habittracker.core.domain.model.HabitDraft
import com.example.habittracker.core.domain.model.HabitIconId
import com.example.habittracker.core.domain.model.HabitId
import com.example.habittracker.core.domain.model.HabitIcons
import com.example.habittracker.core.domain.model.WeekdayMask

fun HabitEntity.toDomain(): Habit {
    val storedIcon = iconId.takeIf { it.isNotBlank() }?.let(::HabitIconId)
    return Habit(
        id = HabitId(id),
        name = name,
        iconId = if (storedIcon != null && HabitIcons.isKnown(storedIcon)) {
            storedIcon
        } else {
            HabitIcons.fallback
        },
        weekdayMask = WeekdayMask(weekdayMask),
        creationDate = creationDate,
    )
}

fun HabitCompletionEntity.toDomain(): HabitCompletion = HabitCompletion(
    habitId = HabitId(habitId),
    date = date,
)

fun HabitDraft.toEntity(): HabitEntity = HabitEntity(
    name = name,
    iconId = iconId.value,
    weekdayMask = weekdayMask.value,
    creationDate = creationDate,
)

fun Habit.toEntity(): HabitEntity = HabitEntity(
    id = id.value,
    name = name,
    iconId = iconId.value,
    weekdayMask = weekdayMask.value,
    creationDate = creationDate,
)

fun List<HabitEntity>.toDomain(): List<Habit> = map(HabitEntity::toDomain)

fun List<HabitCompletionEntity>.toCompletionDomain(): List<HabitCompletion> =
    map(HabitCompletionEntity::toDomain)
