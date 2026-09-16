package com.example.habittracker.di

import com.example.habittracker.core.domain.statistics.HabitStatisticsCalculator
import com.example.habittracker.core.domain.validation.HabitValidator
import com.example.habittracker.data.local.HabitDatabase
import com.example.habittracker.data.local.dao.HabitCompletionDao
import com.example.habittracker.data.local.dao.HabitDao
import com.example.habittracker.data.repository.HabitRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<HabitDatabase> { HabitDatabase.create(androidContext()) }
    single<HabitDao> { get<HabitDatabase>().habitDao() }
    single<HabitCompletionDao> { get<HabitDatabase>().habitCompletionDao() }
    single<HabitValidator> { HabitValidator() }
    single<HabitStatisticsCalculator> { HabitStatisticsCalculator() }
    single<HabitRepository> { HabitRepository(get(), get(), get(), get(), get()) }
}
