package com.example.habittracker.core.domain.usecase

import com.example.habittracker.core.domain.model.Habit
import com.example.habittracker.core.domain.model.HabitId
import com.example.habittracker.core.domain.model.HabitResult
import com.example.habittracker.core.domain.model.HabitUpdate
import com.example.habittracker.data.repository.HabitRepository

class UpdateHabitUseCase(
    private val repository: HabitRepository,
) {
    suspend operator fun invoke(
        habitId: HabitId,
        update: HabitUpdate,
    ): HabitResult<Habit> = repository.updateHabit(habitId, update)
}
