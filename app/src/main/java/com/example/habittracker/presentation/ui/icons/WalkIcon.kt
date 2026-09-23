package com.example.habittracker.presentation.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector

private val walkIconVector: ImageVector = habitIconVector("DirectionsWalk") {
    moveTo(13.5f, 5.5f)
    curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
    reflectiveCurveToRelative(-0.9f, -2.0f, -2.0f, -2.0f)
    reflectiveCurveToRelative(-2.0f, 0.9f, -2.0f, 2.0f)
    reflectiveCurveToRelative(0.9f, 2.0f, 2.0f, 2.0f)
    close()
    moveTo(9.8f, 8.9f)
    lineTo(7.0f, 23.0f)
    horizontalLineToRelative(2.1f)
    lineToRelative(1.8f, -8.0f)
    lineToRelative(2.1f, 2.0f)
    verticalLineToRelative(6.0f)
    horizontalLineToRelative(2.0f)
    verticalLineToRelative(-7.5f)
    lineToRelative(-2.1f, -2.0f)
    lineToRelative(0.6f, -3.0f)
    curveTo(14.8f, 12.0f, 16.8f, 13.0f, 19.0f, 13.0f)
    verticalLineToRelative(-2.0f)
    curveToRelative(-1.9f, 0.0f, -3.5f, -1.0f, -4.3f, -2.4f)
    lineToRelative(-1.0f, -1.6f)
    curveToRelative(-0.56f, -0.89f, -1.68f, -1.25f, -2.65f, -0.84f)
    lineTo(6.0f, 8.3f)
    verticalLineTo(13.0f)
    horizontalLineToRelative(2.0f)
    verticalLineTo(9.6f)
    lineToRelative(1.8f, -0.7f)
}

@Suppress("UnusedReceiverParameter")
internal val HabitTrackerIcons.walkIcon: ImageVector
    get() = walkIconVector
