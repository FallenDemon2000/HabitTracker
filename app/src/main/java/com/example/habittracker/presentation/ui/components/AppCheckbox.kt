package com.example.habittracker.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habittracker.presentation.theme.HabitTrackerTheme
import com.example.habittracker.presentation.ui.icons.HabitTrackerIcons
import com.example.habittracker.presentation.ui.icons.checkIcon

@Composable
fun AppCheckbox(
    checked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scheme = MaterialTheme.colorScheme
    val borderColor = if (checked) scheme.primary else scheme.outline
    val backgroundColor = if (checked) scheme.primary else Color.Transparent
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .border(2.dp, borderColor, CircleShape)
            .clickable { onCheckedChange() },
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Icon(
                imageVector = HabitTrackerIcons.checkIcon,
                contentDescription = "Check icon",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Preview
@Composable
fun AppCheckboxCheckedPreview() {
    HabitTrackerTheme {
        AppCheckbox(
            checked = true,
            onCheckedChange = {},
            modifier = Modifier.size(30.dp)
        )
    }
}

@Preview
@Composable
fun AppCheckboxUncheckedPreview() {
    HabitTrackerTheme {
        AppCheckbox(
            checked = false,
            onCheckedChange = {},
            modifier = Modifier.size(30.dp)
        )
    }
}
