package com.example.habittracker

import android.app.Application
import com.example.habittracker.di.dataModule
import com.example.habittracker.di.domainModule
import com.example.habittracker.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HabitTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@HabitTrackerApplication)
            modules(dataModule, domainModule, presentationModule)
        }
    }
}
