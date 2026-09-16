package com.example.habittracker.core.domain.usecase

import com.example.habittracker.core.domain.model.HabitId
import com.example.habittracker.core.domain.model.HabitResult
import com.example.habittracker.data.repository.HabitRepository
import java.time.ZonedDateTime

class ToggleHabitCompletionUseCase(
    private val repository: HabitRepository,
) {
    suspend operator fun invoke(
        habitId: HabitId,
        date: ZonedDateTime,
        today: ZonedDateTime = ZonedDateTime.now(),
    ): HabitResult<Boolean> = repository.toggleCompletion(habitId, date, today)
}
