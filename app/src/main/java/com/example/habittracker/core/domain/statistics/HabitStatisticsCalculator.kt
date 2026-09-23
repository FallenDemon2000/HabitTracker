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
import java.time.ZonedDateTime

/**
 * Computes all presentation-ready metrics from a supplied snapshot.
 *
 * Public date values retain their zone. Calendar calculations deliberately use
 * local dates so a completion remains a completion for one calendar day.
 */
class HabitStatisticsCalculator {
    fun todayProgress(
        habits: List<Habit>,
        completions: List<HabitCompletion>,
        today: ZonedDateTime,
    ): TodayProgress {
        val completionDates = completionDatesByHabit(completions)
        val todayDate = today.toLocalDate()
        val scheduled = habits
            .filter { todayDate >= it.creationDate.toLocalDate() && isScheduled(it, todayDate, today.zone) }
            .map { habit ->
                TodayHabit(
                    habit = habit,
                    completed = todayDate in completionDates[habit.id].orEmpty(),
                    currentStreak = currentStreak(habit, completionDates[habit.id].orEmpty(), todayDate, today.zone),
                )
            }
        return TodayProgress(today, scheduled)
    }

    fun statistics(
        habits: List<Habit>,
        completions: List<HabitCompletion>,
        today: ZonedDateTime,
    ): HabitStatistics {
        val completionDates = completionDatesByHabit(completions)
        val todayDate = today.toLocalDate()
        val streaks = habits.map { habit ->
            val dates = completionDates[habit.id].orEmpty()
            HabitStreak(
                habitId = habit.id,
                name = habit.name,
                icon = habit.icon,
                current = currentStreak(habit, dates, todayDate, today.zone),
                best = bestStreak(habit, dates, todayDate, today.zone),
            )
        }
        return HabitStatistics(
            today = todayProgress(habits, completions, today),
            currentWeekPercentage = currentWeekPercentage(habits, completionDates, todayDate, today.zone),
            activeCount = habits.size,
            heatmap = fourWeekHeatmap(habits, completionDates, todayDate, today.zone),
            streaks = streaks,
        )
    }

    private fun currentWeekPercentage(
        habits: List<Habit>,
        completionDates: Map<HabitId, Set<LocalDate>>,
        today: LocalDate,
        zone: java.time.ZoneId,
    ): Int {
        val monday = today.with(DayOfWeek.MONDAY)
        var scheduled = 0
        var completed = 0
        habits.forEach { habit ->
            var date = maxOf(habit.creationDate.toLocalDate(), monday)
            while (!date.isAfter(today)) {
                if (isScheduled(habit, date, zone)) {
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
        zone: java.time.ZoneId,
    ): List<HeatmapCell> {
        val firstMonday = today.with(DayOfWeek.MONDAY).minusWeeks(3)
        return (0L until 28L).map { offset ->
            val date = firstMonday.plusDays(offset)
            val zonedDate = date.atStartOfDay(zone)
            if (date.isAfter(today)) {
                HeatmapCell(zonedDate, 0, 0, null)
            } else {
                var scheduled = 0
                var completed = 0
                habits.forEach { habit ->
                    if (!date.isBefore(habit.creationDate.toLocalDate()) && isScheduled(habit, date, zone)) {
                        scheduled++
                        if (date in completionDates[habit.id].orEmpty()) completed++
                    }
                }
                HeatmapCell(zonedDate, completed, scheduled, percentageOrNull(completed, scheduled))
            }
        }
    }

    private fun currentStreak(
        habit: Habit,
        completionDates: Set<LocalDate>,
        today: LocalDate,
        zone: java.time.ZoneId,
    ): Int {
        if (today.isBefore(habit.creationDate.toLocalDate())) return 0

        var cursor = if (isScheduled(habit, today, zone) && today in completionDates) {
            today
        } else {
            previousScheduledOnOrBefore(habit, today.minusDays(1), zone)
        } ?: return 0

        var streak = 0
        while (!cursor.isBefore(habit.creationDate.toLocalDate())) {
            if (!isScheduled(habit, cursor, zone) || cursor !in completionDates) break
            streak++
            cursor = previousScheduledOnOrBefore(habit, cursor.minusDays(1), zone) ?: break
        }
        return streak
    }

    private fun bestStreak(
        habit: Habit,
        completionDates: Set<LocalDate>,
        today: LocalDate,
        zone: java.time.ZoneId,
    ): Int {
        if (today.isBefore(habit.creationDate.toLocalDate())) return 0
        var date = habit.creationDate.toLocalDate()
        var current = 0
        var best = 0
        while (!date.isAfter(today)) {
            if (isScheduled(habit, date, zone)) {
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

    private fun previousScheduledOnOrBefore(
        habit: Habit,
        date: LocalDate,
        zone: java.time.ZoneId,
    ): LocalDate? {
        var cursor = date
        while (!cursor.isBefore(habit.creationDate.toLocalDate())) {
            if (isScheduled(habit, cursor, zone)) return cursor
            cursor = cursor.minusDays(1)
        }
        return null
    }

    private fun isScheduled(habit: Habit, date: LocalDate, zone: java.time.ZoneId): Boolean {
        return habit.schedule.isScheduled(date.atStartOfDay(zone))
    }

    private fun completionDatesByHabit(
        completions: List<HabitCompletion>,
    ): Map<HabitId, Set<LocalDate>> {
        return completions.groupBy(HabitCompletion::habitId)
            .mapValues { (_, values) -> values.mapTo(mutableSetOf()) { it.date.toLocalDate() } }
    }

    private fun percentage(completed: Int, scheduled: Int): Int {
        return if (scheduled == 0) 0 else completed * 100 / scheduled
    }

    private fun percentageOrNull(completed: Int, scheduled: Int): Int? {
        return if (scheduled == 0) 0 else percentage(completed, scheduled)
    }
}
