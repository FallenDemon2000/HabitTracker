package com.example.habittracker.core.domain.mapper

import com.example.habittracker.core.domain.model.Habit
import com.example.habittracker.core.domain.model.HabitStatistics
import com.example.habittracker.core.domain.model.TodayProgress
import com.example.habittracker.presentation.viewmodel.HabitItemUi
import com.example.habittracker.presentation.viewmodel.StatsUiState
import com.example.habittracker.presentation.viewmodel.TodayUiState

fun Habit.toHabitItemUi(completed: Boolean = false, currentStreak: Int = 0): HabitItemUi = HabitItemUi(
    id = id.value,
    name = name,
    icon = icon,
    completed = completed,
    currentStreak = currentStreak,
)

fun TodayProgress.toTodayUiState(): TodayUiState {
    val habits = habits.map { item ->
        HabitItemUi(
            id = item.habit.id.value,
            name = item.habit.name,
            icon = item.habit.icon,
            completed = item.completed,
            currentStreak = item.currentStreak,
        )
    }

    return TodayUiState(
        dateLabel = date.toLocalDate().toString(),
        habits = habits,
        progressText = "${completedCount} / ${scheduledCount}",
        progress = if (scheduledCount == 0) 0f else completedCount.toFloat() / scheduledCount.toFloat(),
    )
}

fun HabitStatistics.toStatsUiState(): StatsUiState = StatsUiState(
    currentWeekPercentage = currentWeekPercentage,
    bestStreak = streaks.maxOfOrNull { it.best } ?: 0,
    activeCount = activeCount,
    heatmap = heatmap,
    streaks = streaks,
)
