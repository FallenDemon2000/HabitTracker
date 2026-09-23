package com.example.habittracker.presentation.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector

private val waterIconVector: ImageVector = habitIconVector("Water") {
    moveTo(12.0f, 2.0f)
    curveToRelative(-5.33f, 4.55f, -8.0f, 8.48f, -8.0f, 11.8f)
    curveToRelative(0.0f, 4.98f, 3.8f, 8.2f, 8.0f, 8.2f)
    reflectiveCurveToRelative(8.0f, -3.22f, 8.0f, -8.2f)
    curveTo(20.0f, 10.48f, 17.33f, 6.55f, 12.0f, 2.0f)
    close()
    moveTo(12.0f, 20.0f)
    curveToRelative(-3.35f, 0.0f, -6.0f, -2.57f, -6.0f, -6.2f)
    curveToRelative(0.0f, -2.34f, 1.95f, -5.44f, 6.0f, -9.14f)
    curveToRelative(4.05f, 3.7f, 6.0f, 6.79f, 6.0f, 9.14f)
    curveTo(18.0f, 17.43f, 15.35f, 20.0f, 12.0f, 20.0f)
    close()
    moveTo(7.83f, 14.0f)
    curveToRelative(0.37f, 0.0f, 0.67f, 0.26f, 0.74f, 0.62f)
    curveToRelative(0.41f, 2.22f, 2.28f, 2.98f, 3.64f, 2.87f)
    curveToRelative(0.43f, -0.02f, 0.79f, 0.32f, 0.79f, 0.75f)
    curveToRelative(0.0f, 0.4f, -0.32f, 0.73f, -0.72f, 0.75f)
    curveToRelative(-2.13f, 0.13f, -4.62f, -1.09f, -5.19f, -4.12f)
    curveTo(7.01f, 14.42f, 7.37f, 14.0f, 7.83f, 14.0f)
    close()
}

@Suppress("UnusedReceiverParameter")
internal val HabitTrackerIcons.waterIcon: ImageVector
    get() = waterIconVector
