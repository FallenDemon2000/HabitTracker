package com.example.habittracker.presentation.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector

private val workoutIconVector: ImageVector = habitIconVector("FitnessCenter") {
    moveTo(20.57f, 14.86f)
    lineTo(22.0f, 13.43f)
    lineTo(20.57f, 12.0f)
    lineTo(17.0f, 15.57f)
    lineTo(8.43f, 7.0f)
    lineTo(12.0f, 3.43f)
    lineTo(10.57f, 2.0f)
    lineTo(9.14f, 3.43f)
    lineTo(7.71f, 2.0f)
    lineTo(5.57f, 4.14f)
    lineTo(4.14f, 2.71f)
    lineTo(2.71f, 4.14f)
    lineToRelative(1.43f, 1.43f)
    lineTo(2.0f, 7.71f)
    lineToRelative(1.43f, 1.43f)
    lineTo(2.0f, 10.57f)
    lineTo(3.43f, 12.0f)
    lineTo(7.0f, 8.43f)
    lineTo(15.57f, 17.0f)
    lineTo(12.0f, 20.57f)
    lineTo(13.43f, 22.0f)
    lineToRelative(1.43f, -1.43f)
    lineTo(16.29f, 22.0f)
    lineToRelative(2.14f, -2.14f)
    lineToRelative(1.43f, 1.43f)
    lineToRelative(1.43f, -1.43f)
    lineToRelative(-1.43f, -1.43f)
    lineTo(22.0f, 16.29f)
    lineToRelative(-1.43f, -1.43f)
    close()
}

@Suppress("UnusedReceiverParameter")
internal val HabitTrackerIcons.workoutIcon: ImageVector
    get() = workoutIconVector
