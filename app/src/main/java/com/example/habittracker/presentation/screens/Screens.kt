package com.example.habittracker.presentation.screens

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screens {
    @Serializable
    data object Today : Screens

    @Serializable
    data object Statistics : Screens

    @Serializable
    data object CreateHabit : Screens

    @Serializable
    data class EditHabit(val habitId: Long) : Screens
}
