package com.example.habittracker.core.domain.usecase

import com.example.habittracker.core.domain.model.Habit
import com.example.habittracker.data.repository.HabitRepository
import kotlinx.coroutines.flow.Flow

class ObserveHabitsUseCase(
    private val repository: HabitRepository,
) {
    operator fun invoke(): Flow<List<Habit>> = repository.observeHabits()
}
