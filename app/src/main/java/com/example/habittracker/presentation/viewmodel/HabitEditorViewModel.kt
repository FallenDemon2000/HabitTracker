package com.example.habittracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.core.domain.model.HabitDraft
import com.example.habittracker.core.domain.model.HabitIcon
import com.example.habittracker.core.domain.model.HabitId
import com.example.habittracker.core.domain.model.HabitResult
import com.example.habittracker.core.domain.model.HabitSchedule
import com.example.habittracker.core.domain.model.HabitUpdate
import com.example.habittracker.core.domain.usecase.CreateHabitUseCase
import com.example.habittracker.core.domain.usecase.DeleteHabitUseCase
import com.example.habittracker.core.domain.usecase.GetHabitUseCase
import com.example.habittracker.core.domain.usecase.UpdateHabitUseCase
import com.example.habittracker.presentation.screens.HabitEditorMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.ZonedDateTime

data class HabitEditorUiState(
    val name: String = "",
    val icon: HabitIcon = HabitIcon.RUN,
    val selectedDays: Set<DayOfWeek> = emptySet(),
)

class HabitEditorViewModel(
    private val getHabitUseCase: GetHabitUseCase,
    private val createHabitUseCase: CreateHabitUseCase,
    private val updateHabitUseCase: UpdateHabitUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HabitEditorUiState())
    val uiState: StateFlow<HabitEditorUiState> = _uiState.asStateFlow()

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onIconChanged(icon: HabitIcon) {
        _uiState.update { it.copy(icon = icon) }
    }

    fun onDaysChanged(days: Set<DayOfWeek>) {
        _uiState.update { it.copy(selectedDays = days) }
    }

    fun loadHabit(habitId: Long?) {
        if (habitId == null) return
        viewModelScope.launch {
            val id = HabitId(habitId)
            val habit = getHabitUseCase(id)
            if (habit is HabitResult.Success) {
                val habitValue = habit.value
                _uiState.update {
                    it.copy(
                        name = habitValue.name,
                        icon = habitValue.icon,
                        selectedDays = habitValue.schedule.toSelectedDays(),
                    )
                }
            }
        }
    }

    fun saveHabit(
        mode: HabitEditorMode,
        name: String,
        icon: HabitIcon,
        selectedDays: Set<DayOfWeek>,
    ) {
        viewModelScope.launch {
            val schedule = selectedDays.toHabitSchedule()
            val draft = HabitDraft(
                name = name,
                icon = icon,
                schedule = schedule,
                creationDate = ZonedDateTime.now(),
            )

            if (mode == HabitEditorMode.CREATE) {
                createHabitUseCase(draft)
            } else {
                val habitId = HabitId(_uiState.value.name.hashCode().toLong())
                val update = HabitUpdate(
                    name = name,
                    icon = icon,
                    schedule = schedule,
                )
                updateHabitUseCase(habitId, update)
            }
        }
    }

    fun deleteHabit(habitId: HabitId) {
        viewModelScope.launch {
            deleteHabitUseCase(habitId)
        }
    }

    private fun Set<DayOfWeek>.toHabitSchedule(): HabitSchedule = HabitSchedule(
        monday = contains(DayOfWeek.MONDAY),
        tuesday = contains(DayOfWeek.TUESDAY),
        wednesday = contains(DayOfWeek.WEDNESDAY),
        thursday = contains(DayOfWeek.THURSDAY),
        friday = contains(DayOfWeek.FRIDAY),
        saturday = contains(DayOfWeek.SATURDAY),
        sunday = contains(DayOfWeek.SUNDAY),
    )

    private fun HabitSchedule.toSelectedDays(): Set<DayOfWeek> =
        buildSet {
            if (monday) add(DayOfWeek.MONDAY)
            if (tuesday) add(DayOfWeek.TUESDAY)
            if (wednesday) add(DayOfWeek.WEDNESDAY)
            if (thursday) add(DayOfWeek.THURSDAY)
            if (friday) add(DayOfWeek.FRIDAY)
            if (saturday) add(DayOfWeek.SATURDAY)
            if (sunday) add(DayOfWeek.SUNDAY)
    }
}
