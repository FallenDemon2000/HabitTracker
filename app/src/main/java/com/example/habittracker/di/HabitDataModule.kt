package com.example.habittracker.di

import com.example.habittracker.core.domain.statistics.HabitStatisticsCalculator
import com.example.habittracker.core.domain.validation.HabitValidator
import com.example.habittracker.data.local.HabitDatabase
import com.example.habittracker.data.repository.HabitRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val habitDataModule = module {
    single { HabitDatabase.create(androidContext()) }
    single { get<HabitDatabase>().habitDao() }
    single { get<HabitDatabase>().habitCompletionDao() }
    single { HabitValidator() }
    single { HabitStatisticsCalculator() }
    single { HabitRepository(get(), get(), get(), get(), get()) }
}
