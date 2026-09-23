package com.example.habittracker.presentation.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector

private val codeIconVector: ImageVector = habitIconVector("Code") {
    moveTo(9.4f, 16.6f)
    lineTo(4.8f, 12.0f)
    lineToRelative(4.6f, -4.6f)
    lineTo(8.0f, 6.0f)
    lineToRelative(-6.0f, 6.0f)
    lineToRelative(6.0f, 6.0f)
    lineToRelative(1.4f, -1.4f)
    close()
    moveTo(14.6f, 16.6f)
    lineToRelative(4.6f, -4.6f)
    lineToRelative(-4.6f, -4.6f)
    lineTo(16.0f, 6.0f)
    lineToRelative(6.0f, 6.0f)
    lineToRelative(-6.0f, 6.0f)
    lineToRelative(-1.4f, -1.4f)
    close()
}

@Suppress("UnusedReceiverParameter")
internal val HabitTrackerIcons.codeIcon: ImageVector
    get() = codeIconVector
