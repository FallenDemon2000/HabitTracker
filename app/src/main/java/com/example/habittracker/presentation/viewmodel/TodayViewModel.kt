package com.example.habittracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.core.domain.mapper.toTodayUiState
import com.example.habittracker.core.domain.model.HabitId
import com.example.habittracker.core.domain.usecase.ObserveTodayUseCase
import com.example.habittracker.core.domain.usecase.ToggleHabitCompletionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

data class HabitItemUi(
    val id: Long,
    val name: String,
    val icon: com.example.habittracker.core.domain.model.HabitIcon,
    val completed: Boolean,
    val currentStreak: Int,
)

data class TodayUiState(
    val dateLabel: String = "",
    val habits: List<HabitItemUi> = emptyList(),
    val progressText: String = "0 / 0",
    val progress: Float = 0f,
)

class TodayViewModel(
    private val observeTodayUseCase: ObserveTodayUseCase,
    private val toggleHabitCompletionUseCase: ToggleHabitCompletionUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    private val today: ZonedDateTime = ZonedDateTime.now()

    init {
        observeToday()
    }

    fun observeToday(date: ZonedDateTime = today) {
        viewModelScope.launch {
            observeTodayUseCase(date).collectLatest { progress ->
                _uiState.value = progress.toTodayUiState()
            }
        }
    }

    fun onToggleHabit(habitId: Long, date: ZonedDateTime = today) {
        viewModelScope.launch {
            toggleHabitCompletionUseCase(HabitId(habitId), date, today)
        }
    }
}
