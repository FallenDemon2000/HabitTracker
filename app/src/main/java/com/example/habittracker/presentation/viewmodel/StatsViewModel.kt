package com.example.habittracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.core.domain.mapper.toStatsUiState
import com.example.habittracker.core.domain.usecase.ObserveStatisticsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

data class StatsUiState(
    val currentWeekPercentage: Int = 0,
    val bestStreak: Int = 0,
    val activeCount: Int = 0,
)

class StatsViewModel(
    private val observeStatisticsUseCase: ObserveStatisticsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        observeStatistics()
    }

    fun observeStatistics(date: ZonedDateTime = ZonedDateTime.now()) {
        viewModelScope.launch {
            observeStatisticsUseCase(date).collectLatest { statistics ->
                _uiState.value = statistics.toStatsUiState()
            }
        }
    }
}
