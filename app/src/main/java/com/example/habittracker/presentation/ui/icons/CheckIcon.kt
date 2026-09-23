package com.example.habittracker.presentation.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector

private val checkIconVector: ImageVector = habitIconVector("Check") {
    moveTo(9.0f, 16.17f)
    lineTo(4.83f, 12.0f)
    lineToRelative(-1.42f, 1.41f)
    lineTo(9.0f, 19.0f)
    lineTo(21.0f, 7.0f)
    lineToRelative(-1.41f, -1.41f)
    close()
}

@Suppress("UnusedReceiverParameter")
internal val HabitTrackerIcons.checkIcon: ImageVector
    get() = checkIconVector
