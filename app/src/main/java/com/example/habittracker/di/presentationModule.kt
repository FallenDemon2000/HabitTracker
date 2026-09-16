package com.example.habittracker.di

import com.example.habittracker.presentation.viewmodel.HabitEditorViewModel
import com.example.habittracker.presentation.viewmodel.StatsViewModel
import com.example.habittracker.presentation.viewmodel.TodayViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::TodayViewModel)
    viewModelOf(::StatsViewModel)
    viewModelOf(::HabitEditorViewModel)
}
