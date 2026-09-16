package com.example.habittracker.core.domain.model

sealed interface HabitResult<out T> {
    data class Success<T>(val value: T) : HabitResult<T>

    data class Failure(val error: HabitError) : HabitResult<Nothing>
}
