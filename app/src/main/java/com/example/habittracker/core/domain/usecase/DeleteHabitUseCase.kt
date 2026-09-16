package com.example.habittracker.core.domain.usecase

import com.example.habittracker.core.domain.error.HabitResult
import com.example.habittracker.core.domain.model.HabitId
import com.example.habittracker.data.repository.HabitRepository

class DeleteHabitUseCase(
    private val repository: HabitRepository,
) {
    suspend operator fun invoke(habitId: HabitId): HabitResult<Unit> = repository.deleteHabit(habitId)
}
