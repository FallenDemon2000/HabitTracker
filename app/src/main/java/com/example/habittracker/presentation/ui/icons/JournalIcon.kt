package com.example.habittracker.presentation.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector

private val journalIconVector: ImageVector = habitIconVector("Article") {
    moveTo(19.0f, 5.0f)
    verticalLineToRelative(14.0f)
    horizontalLineTo(5.0f)
    verticalLineTo(5.0f)
    horizontalLineTo(19.0f)
    moveTo(19.0f, 3.0f)
    horizontalLineTo(5.0f)
    curveTo(3.9f, 3.0f, 3.0f, 3.9f, 3.0f, 5.0f)
    verticalLineToRelative(14.0f)
    curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
    horizontalLineToRelative(14.0f)
    curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
    verticalLineTo(5.0f)
    curveTo(21.0f, 3.9f, 20.1f, 3.0f, 19.0f, 3.0f)
    lineTo(19.0f, 3.0f)
    close()
    moveTo(14.0f, 17.0f)
    horizontalLineTo(7.0f)
    verticalLineToRelative(-2.0f)
    horizontalLineToRelative(7.0f)
    verticalLineTo(17.0f)
    close()
    moveTo(17.0f, 13.0f)
    horizontalLineTo(7.0f)
    verticalLineToRelative(-2.0f)
    horizontalLineToRelative(10.0f)
    verticalLineTo(13.0f)
    close()
    moveTo(17.0f, 9.0f)
    horizontalLineTo(7.0f)
    verticalLineTo(7.0f)
    horizontalLineToRelative(10.0f)
    verticalLineTo(9.0f)
    close()
}

@Suppress("UnusedReceiverParameter")
internal val HabitTrackerIcons.journalIcon: ImageVector
    get() = journalIconVector
