package com.example.habittracker.presentation.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector

private val addIconVector: ImageVector = habitIconVector("Add") {
    moveTo(11f, 3f)
    lineTo(13f, 3f)
    lineTo(13f, 11f)
    lineTo(21f, 11f)
    lineTo(21f, 13f)
    lineTo(13f, 13f)
    lineTo(13f, 21f)
    lineTo(11f, 21f)
    lineTo(11f, 13f)
    lineTo(3f, 13f)
    lineTo(3f, 11f)
    lineTo(11f, 11f)
    close()
}

@Suppress("UnusedReceiverParameter")
internal val HabitTrackerIcons.addIcon: ImageVector
    get() = addIconVector
