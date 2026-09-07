package com.example.habittracker.core.domain.statistics

import com.example.habittracker.core.domain.model.Habit
import com.example.habittracker.core.domain.model.HabitCompletion
import com.example.habittracker.core.domain.model.HabitId
import com.example.habittracker.core.domain.model.HabitStatistics
import com.example.habittracker.core.domain.model.HeatmapCell
import com.example.habittracker.core.domain.model.HabitStreak
import com.example.habittracker.core.domain.model.TodayHabit
import com.example.habittracker.core.domain.model.TodayProgress
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * Computes all presentation-ready metrics from a supplied snapshot.
 *
 * Supplying [today] keeps calculations deterministic and makes the date boundary
 * explicit: denominators never include dates before creation or after today.
 */
class HabitStatisticsCalculator {
    fun todayProgress(
        habits: List<Habit>,
        completions: List<HabitCompletion>,
        today: LocalDate,
    ): TodayProgress {
        val completionDates = completionDatesByHabit(completions)
        val scheduled = habits
            .filter { !today.isBefore(it.creationDate) && it.weekdayMask.isScheduled(today) }
            .map { habit ->
                TodayHabit(
                    habit = habit,
                    completed = today in completionDates[habit.id].orEmpty(),
                    currentStreak = currentStreak(habit, completionDates[habit.id].orEmpty(), today),
                )
            }
        return TodayProgress(today, scheduled)
    }

    fun statistics(
        habits: List<Habit>,
        completions: List<HabitCompletion>,
        today: LocalDate,
    ): HabitStatistics {
        val completionDates = completionDatesByHabit(completions)
        val streaks = habits.map { habit ->
            val dates = completionDates[habit.id].orEmpty()
            HabitStreak(
                habitId = habit.id,
                current = currentStreak(habit, dates, today),
                best = bestStreak(habit, dates, today),
            )
        }
        return HabitStatistics(
            today = todayProgress(habits, completions, today),
            currentWeekPercentage = currentWeekPercentage(habits, completionDates, today),
            activeCount = habits.size,
            heatmap = fourWeekHeatmap(habits, completionDates, today),
            streaks = streaks,
        )
    }

    private fun currentWeekPercentage(
        habits: List<Habit>,
        completionDates: Map<HabitId, Set<LocalDate>>,
        today: LocalDate,
    ): Int {
        val monday = today.with(DayOfWeek.MONDAY)
        var scheduled = 0
        var completed = 0
        habits.forEach { habit ->
            var date = maxOf(habit.creationDate, monday)
            while (!date.isAfter(today)) {
                if (habit.weekdayMask.isScheduled(date)) {
                    scheduled++
                    if (date in completionDates[habit.id].orEmpty()) completed++
                }
                date = date.plusDays(1)
            }
        }
        return percentage(completed, scheduled)
    }

    private fun fourWeekHeatmap(
        habits: List<Habit>,
        completionDates: Map<HabitId, Set<LocalDate>>,
        today: LocalDate,
    ): List<HeatmapCell> {
        val firstMonday = today.with(DayOfWeek.MONDAY).minusWeeks(3)
        return (0L until 28L).map { offset ->
            val date = firstMonday.plusDays(offset)
            if (date.isAfter(today)) {
                HeatmapCell(date, 0, 0, null)
            } else {
                var scheduled = 0
                var completed = 0
                habits.forEach { habit ->
                    if (!date.isBefore(habit.creationDate) && habit.weekdayMask.isScheduled(date)) {
                        scheduled++
                        if (date in completionDates[habit.id].orEmpty()) completed++
                    }
                }
                HeatmapCell(date, completed, scheduled, percentageOrNull(completed, scheduled))
            }
        }
    }

    private fun currentStreak(
        habit: Habit,
        completionDates: Set<LocalDate>,
        today: LocalDate,
    ): Int {
        if (today.isBefore(habit.creationDate)) return 0

        var cursor = if (
            habit.weekdayMask.isScheduled(today) && today in completionDates
        ) {
            today
        } else {
            previousScheduledOnOrBefore(habit, today.minusDays(1))
        } ?: return 0

        var streak = 0
        while (!cursor.isBefore(habit.creationDate)) {
            if (!habit.weekdayMask.isScheduled(cursor) || cursor !in completionDates) break
            streak++
            cursor = previousScheduledOnOrBefore(habit, cursor.minusDays(1)) ?: break
        }
        return streak
    }

    private fun bestStreak(
        habit: Habit,
        completionDates: Set<LocalDate>,
        today: LocalDate,
    ): Int {
        if (today.isBefore(habit.creationDate)) return 0
        var date = habit.creationDate
        var current = 0
        var best = 0
        while (!date.isAfter(today)) {
            if (habit.weekdayMask.isScheduled(date)) {
                if (date in completionDates) {
                    current++
                    best = maxOf(best, current)
                } else {
                    current = 0
                }
            }
            date = date.plusDays(1)
        }
        return best
    }

    private fun previousScheduledOnOrBefore(habit: Habit, date: LocalDate): LocalDate? {
        var cursor = date
        while (!cursor.isBefore(habit.creationDate)) {
            if (habit.weekdayMask.isScheduled(cursor)) return cursor
            cursor = cursor.minusDays(1)
        }
        return null
    }

    private fun completionDatesByHabit(
        completions: List<HabitCompletion>,
    ): Map<HabitId, Set<LocalDate>> {
        return completions.groupBy(HabitCompletion::habitId)
            .mapValues { (_, values) -> values.mapTo(mutableSetOf(), HabitCompletion::date) }
    }

    private fun percentage(completed: Int, scheduled: Int): Int {
        return if (scheduled == 0) 0 else completed * 100 / scheduled
    }

    private fun percentageOrNull(completed: Int, scheduled: Int): Int? {
        return if (scheduled == 0) 0 else percentage(completed, scheduled)
    }
}
