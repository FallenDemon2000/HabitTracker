package com.example.habittracker.presentation.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector

private val phoneIconVector: ImageVector = habitIconVector("Smartphone") {
    moveTo(17.0f, 1.01f)
    lineTo(7.0f, 1.0f)
    curveToRelative(-1.1f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
    verticalLineToRelative(18.0f)
    curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
    horizontalLineToRelative(10.0f)
    curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
    verticalLineTo(3.0f)
    curveToRelative(0.0f, -1.1f, -0.9f, -1.99f, -2.0f, -1.99f)
    close()
    moveTo(17.0f, 19.0f)
    horizontalLineTo(7.0f)
    verticalLineTo(5.0f)
    horizontalLineToRelative(10.0f)
    verticalLineToRelative(14.0f)
    close()
}

@Suppress("UnusedReceiverParameter")
internal val HabitTrackerIcons.phoneIcon: ImageVector
    get() = phoneIconVector
