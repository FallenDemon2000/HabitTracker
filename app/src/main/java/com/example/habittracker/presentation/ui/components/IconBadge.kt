package com.example.habittracker.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme

@Composable
fun IconBadge(
    modifier: Modifier = Modifier,
    icon: @Composable BoxScope.() -> Unit,
    hasBorder: Boolean = false,
    size: Dp = 42.dp,
    shape: Shape = RoundedCornerShape(12.dp),
) {
    Box(
        modifier = modifier
            .width(size)
            .height(size)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = if (hasBorder) 1.5.dp else 0.dp,
                color = if (hasBorder) MaterialTheme.colorScheme.secondary else Color.Transparent,
                shape = shape,
            ),
        contentAlignment = Alignment.Center,
        content = icon,
    )
}
