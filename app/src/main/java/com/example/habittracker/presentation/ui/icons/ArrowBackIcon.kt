package com.example.habittracker.presentation.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector

private val arrowBackIconVector: ImageVector = habitIconVector("ArrowBack") {
    moveTo(20f, 11f)
    lineTo(7.5f, 11f)
    lineTo(13.5f, 5f)
    lineTo(12f, 3.5f)
    lineTo(4f, 12f)
    lineTo(12f, 20.5f)
    lineTo(13.5f, 19f)
    lineTo(7.5f, 13f)
    lineTo(20f, 13f)
    close()
}

@Suppress("UnusedReceiverParameter")
internal val HabitTrackerIcons.arrowBackIcon: ImageVector
    get() = arrowBackIconVector
