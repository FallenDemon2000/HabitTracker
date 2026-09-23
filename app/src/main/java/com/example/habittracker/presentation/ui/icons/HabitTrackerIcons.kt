package com.example.habittracker.presentation.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.habittracker.core.domain.model.HabitIcon

object HabitTrackerIcons {
    fun iconFor(icon: HabitIcon): ImageVector = when (icon) {
        HabitIcon.RUN -> runIcon
        HabitIcon.READ -> bookIcon
        HabitIcon.WATER -> waterIcon
        HabitIcon.MEDITATE -> meditationIcon
        HabitIcon.SLEEP -> sleepIcon
        HabitIcon.CODE -> codeIcon
        HabitIcon.MUSIC -> musicIcon
        HabitIcon.COOK -> cookIcon
        HabitIcon.JOURNAL -> journalIcon
        HabitIcon.GYM -> workoutIcon
        HabitIcon.YOGA -> meditationIcon
        HabitIcon.WALK -> walkIcon
        HabitIcon.CYCLE -> cycleIcon
        HabitIcon.STUDY -> studyIcon
        HabitIcon.NO_PHONE -> phoneIcon
        HabitIcon.VITAMINS -> healthIcon
        HabitIcon.LANGUAGE -> languageIcon
        HabitIcon.GRATITUDE -> starIcon
        HabitIcon.HEALTH -> healthIcon
        HabitIcon.ORGANIZE -> organizeIcon
    }
}
