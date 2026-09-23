package com.example.habittracker.presentation.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector

private val checkIconVector: ImageVector = habitIconVector("Check") {
    moveTo(5f, 12.5f)
    lineTo(9.5f, 17f)
    lineTo(19f, 7.5f)
}

@Suppress("UnusedReceiverParameter")
internal val HabitTrackerIcons.checkIcon: ImageVector
    get() = checkIconVector
