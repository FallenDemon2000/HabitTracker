package com.example.habittracker.di

import com.example.habittracker.core.domain.usecase.CreateHabitUseCase
import com.example.habittracker.core.domain.usecase.DeleteHabitUseCase
import com.example.habittracker.core.domain.usecase.ObserveHabitsUseCase
import com.example.habittracker.core.domain.usecase.ObserveStatisticsUseCase
import com.example.habittracker.core.domain.usecase.ObserveTodayUseCase
import com.example.habittracker.core.domain.usecase.ToggleHabitCompletionUseCase
import com.example.habittracker.core.domain.usecase.UpdateHabitUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::ObserveHabitsUseCase)
    singleOf(::ObserveTodayUseCase)
    singleOf(::ObserveStatisticsUseCase)
    singleOf(::CreateHabitUseCase)
    singleOf(::UpdateHabitUseCase)
    singleOf(::DeleteHabitUseCase)
    singleOf(::ToggleHabitCompletionUseCase)
}
