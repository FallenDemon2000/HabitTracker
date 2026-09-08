package com.example.habittracker.core.domain.model

@JvmInline
value class HabitIconId(val value: String) {
    init {
        require(value.isNotBlank()) { "Habit icon IDs must not be blank." }
    }
}
