package com.example.habittracker.core.domain.model

@JvmInline
value class HabitId(val value: Long) {
    init {
        require(value > 0) { "Habit IDs must be positive." }
    }
}
