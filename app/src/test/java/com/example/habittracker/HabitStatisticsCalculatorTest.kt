package com.example.habittracker

import com.example.habittracker.core.domain.model.Habit
import com.example.habittracker.core.domain.model.HabitCompletion
import com.example.habittracker.core.domain.model.HabitIcon
import com.example.habittracker.core.domain.model.HabitId
import com.example.habittracker.core.domain.model.HabitSchedule
import com.example.habittracker.core.domain.statistics.HabitStatisticsCalculator
import java.time.ZoneOffset
import java.time.ZonedDateTime
import org.junit.Assert.assertEquals
import org.junit.Test

class HabitStatisticsCalculatorTest {
    private val calculator = HabitStatisticsCalculator()
    private val zone = ZoneOffset.UTC
    private val habitId = HabitId(1)
    private val today = date(2026, 9, 14)

    @Test
    fun currentStreak_isZeroWithoutCompletions() {
        assertCurrentStreak(0)
    }

    @Test
    fun currentStreak_countsCompletedToday() {
        assertCurrentStreak(1, today)
    }

    @Test
    fun currentStreak_usesYesterdayWhenTodayIsIncomplete() {
        assertCurrentStreak(2, today.minusDays(1), today.minusDays(2))
    }

    @Test
    fun currentStreak_skipsUnscheduledDays() {
        val schedule = weekdays()
        val habit = habit(schedule = schedule)
        val completions = listOf(
            completion(today.minusDays(3)),
            completion(today.minusDays(4)),
        )

        assertEquals(
            2,
            calculator.statistics(listOf(habit), completions, today).streaks.single().current,
        )
    }

    @Test
    fun currentStreak_stopsAtMissingScheduledDay() {
        assertCurrentStreak(1, today)
    }

    @Test
    fun currentStreak_isZeroBeforeHabitCreation() {
        val creationDate = today.plusDays(1)
        val habit = habit(creationDate = creationDate)

        assertEquals(
            0,
            calculator.statistics(listOf(habit), listOf(completion(today)), today)
                .streaks.single().current,
        )
    }

    @Test
    fun currentStreak_ignoresCompletionsBeforeCreation() {
        val creationDate = today.minusDays(2)
        val habit = habit(creationDate = creationDate)

        assertEquals(
            0,
            calculator.statistics(listOf(habit), listOf(completion(today.minusDays(3))), today)
                .streaks.single().current,
        )
    }

    @Test
    fun bestStreak_isZeroWithoutCompletions() {
        assertBestStreak(0)
    }

    @Test
    fun bestStreak_countsOneCompletedScheduledDay() {
        assertBestStreak(1, today)
    }

    @Test
    fun bestStreak_countsLongestConsecutiveScheduledRun() {
        assertBestStreak(
            3,
            today.minusDays(4),
            today.minusDays(3),
            today.minusDays(2),
        )
    }

    @Test
    fun bestStreak_doesNotCountAcrossMissingScheduledDay() {
        assertBestStreak(2, today.minusDays(4), today.minusDays(3), today)
    }

    @Test
    fun bestStreak_skipsUnscheduledDays() {
        val habit = habit(schedule = weekdays())
        val completions = listOf(
            completion(today.minusDays(4)),
            completion(today.minusDays(3)),
            completion(today),
        )

        assertEquals(
            3,
            calculator.statistics(listOf(habit), completions, today).streaks.single().best,
        )
    }

    @Test
    fun bestStreak_ignoresFutureCompletions() {
        assertBestStreak(0, today.plusDays(1))
    }

    @Test
    fun bestStreak_ignoresCompletionsBeforeCreation() {
        val creationDate = today.minusDays(1)
        val habit = habit(creationDate = creationDate)

        assertEquals(
            0,
            calculator.statistics(listOf(habit), listOf(completion(today.minusDays(2))), today)
                .streaks.single().best,
        )
    }

    private fun assertCurrentStreak(
        expected: Int,
        vararg completionDates: ZonedDateTime,
    ) {
        val habit = habit()
        assertEquals(
            expected,
            calculator.statistics(
                habits = listOf(habit),
                completions = completionDates.map(::completion),
                today = today,
            ).streaks.single().current,
        )
    }

    private fun assertBestStreak(
        expected: Int,
        vararg completionDates: ZonedDateTime,
    ) {
        val habit = habit()
        assertEquals(
            expected,
            calculator.statistics(
                habits = listOf(habit),
                completions = completionDates.map(::completion),
                today = today,
            ).streaks.single().best,
        )
    }

    private fun habit(
        schedule: HabitSchedule = everyDay(),
        creationDate: ZonedDateTime = today.minusDays(10),
    ) = Habit(
        id = habitId,
        name = "Read",
        icon = HabitIcon.READ,
        schedule = schedule,
        creationDate = creationDate,
    )

    private fun completion(date: ZonedDateTime) = HabitCompletion(habitId, date)

    private fun everyDay() = HabitSchedule(
        monday = true,
        tuesday = true,
        wednesday = true,
        thursday = true,
        friday = true,
        saturday = true,
        sunday = true,
    )

    private fun weekdays() = HabitSchedule(
        monday = true,
        tuesday = true,
        wednesday = true,
        thursday = true,
        friday = true,
    )

    private fun date(year: Int, month: Int, day: Int): ZonedDateTime {
        return ZonedDateTime.of(year, month, day, 12, 0, 0, 0, zone)
    }
}
