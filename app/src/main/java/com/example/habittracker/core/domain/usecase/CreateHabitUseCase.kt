package com.example.habittracker.core.domain.usecase

import com.example.habittracker.core.domain.model.Habit
import com.example.habittracker.core.domain.model.HabitDraft
import com.example.habittracker.core.domain.model.HabitResult
import com.example.habittracker.data.repository.HabitRepository
import java.time.ZonedDateTime

class CreateHabitUseCase(
    private val repository: HabitRepository,
) {
    suspend operator fun invoke(
        draft: HabitDraft,
        today: ZonedDateTime = ZonedDateTime.now(),
    ): HabitResult<Habit> = repository.createHabit(draft, today)
}
