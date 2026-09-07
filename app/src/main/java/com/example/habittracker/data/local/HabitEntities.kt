package com.example.habittracker.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.example.habittracker.core.domain.model.Habit
import com.example.habittracker.core.domain.model.HabitCompletion
import com.example.habittracker.core.domain.model.HabitDraft
import com.example.habittracker.core.domain.model.HabitId
import com.example.habittracker.core.domain.model.HabitIconId
import com.example.habittracker.core.domain.model.HabitIcons
import com.example.habittracker.core.domain.model.WeekdayMask
import java.time.LocalDate

@Entity(
    tableName = "habits",
)
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "icon_id")
    val iconId: String,
    @ColumnInfo(name = "weekday_mask")
    val weekdayMask: Int,
    @ColumnInfo(name = "creation_date")
    val creationDate: LocalDate,
)

@Entity(
    tableName = "habit_completions",
    primaryKeys = ["habit_id", "date"],
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["id"],
            childColumns = ["habit_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["date", "habit_id"])],
)
data class HabitCompletionEntity(
    @ColumnInfo(name = "habit_id")
    val habitId: Long,
    val date: LocalDate,
)

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
