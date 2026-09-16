package com.example.habittracker.core.domain.usecase

import com.example.habittracker.core.domain.model.HabitStatistics
import com.example.habittracker.data.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

class ObserveStatisticsUseCase(
    private val repository: HabitRepository,
) {
    operator fun invoke(today: ZonedDateTime): Flow<HabitStatistics> = repository.observeStatistics(today)
}
