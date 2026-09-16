package com.example.habittracker.core.domain.usecase

import com.example.habittracker.core.domain.model.TodayProgress
import com.example.habittracker.data.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

class ObserveTodayUseCase(
    private val repository: HabitRepository,
) {
    operator fun invoke(today: ZonedDateTime): Flow<TodayProgress> = repository.observeToday(today)
}
