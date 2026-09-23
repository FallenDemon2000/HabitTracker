package com.example.habittracker.presentation.ui.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.ui.graphics.vector.ImageVector

private val runIconVector: ImageVector = habitIconVector("Run") {
    moveTo(13.49f, 5.48f)
    curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
    reflectiveCurveToRelative(-0.9f, -2.0f, -2.0f, -2.0f)
    reflectiveCurveToRelative(-2.0f, 0.9f, -2.0f, 2.0f)
    reflectiveCurveToRelative(0.9f, 2.0f, 2.0f, 2.0f)
    close()
    moveTo(9.89f, 19.38f)
    lineToRelative(1.0f, -4.4f)
    lineToRelative(2.1f, 2.0f)
    verticalLineToRelative(6.0f)
    horizontalLineToRelative(2.0f)
    verticalLineToRelative(-7.5f)
    lineToRelative(-2.1f, -2.0f)
    lineToRelative(0.6f, -3.0f)
    curveToRelative(1.3f, 1.5f, 3.3f, 2.5f, 5.5f, 2.5f)
    verticalLineToRelative(-2.0f)
    curveToRelative(-1.9f, 0.0f, -3.5f, -1.0f, -4.3f, -2.4f)
    lineToRelative(-1.0f, -1.6f)
    curveToRelative(-0.4f, -0.6f, -1.0f, -1.0f, -1.7f, -1.0f)
    curveToRelative(-0.3f, 0.0f, -0.5f, 0.1f, -0.8f, 0.1f)
    lineToRelative(-5.2f, 2.2f)
    verticalLineToRelative(4.7f)
    horizontalLineToRelative(2.0f)
    verticalLineToRelative(-3.4f)
    lineToRelative(1.8f, -0.7f)
    lineToRelative(-1.6f, 8.1f)
    lineToRelative(-4.9f, -1.0f)
    lineToRelative(-0.4f, 2.0f)
    lineToRelative(7.0f, 1.4f)
    close()
}

@Suppress("UnusedReceiverParameter")
internal val HabitTrackerIcons.runIcon: ImageVector
    get() = runIconVector
