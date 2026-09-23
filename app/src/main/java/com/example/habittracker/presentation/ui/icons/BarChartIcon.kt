package com.example.habittracker.presentation.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector

private val barChartIconVector: ImageVector = habitIconVector("BarChart") {
    moveTo(4f, 20f)
    lineTo(4f, 10f)
    lineTo(7f, 10f)
    lineTo(7f, 20f)
    close()
    moveTo(10f, 20f)
    lineTo(10f, 6f)
    lineTo(13f, 6f)
    lineTo(13f, 20f)
    close()
    moveTo(16f, 20f)
    lineTo(16f, 13f)
    lineTo(19f, 13f)
    lineTo(19f, 20f)
    close()
}

@Suppress("UnusedReceiverParameter")
internal val HabitTrackerIcons.barChartIcon: ImageVector
    get() = barChartIconVector
