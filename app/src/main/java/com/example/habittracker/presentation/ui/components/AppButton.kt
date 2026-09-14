package com.example.habittracker.presentation.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class AppButtonType {
    Primary,
    Secondary,
    DestructiveText,
}

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    type: AppButtonType = AppButtonType.Primary,
) {
    val scheme = MaterialTheme.colorScheme
    val colors = when (type) {
        AppButtonType.Primary -> ButtonDefaults.buttonColors(
            containerColor = scheme.primary,
            contentColor = scheme.onPrimary,
            disabledContainerColor = scheme.primary.copy(alpha = 0.5f),
            disabledContentColor = scheme.onPrimary.copy(alpha = 0.7f),
        )
        AppButtonType.Secondary -> ButtonDefaults.buttonColors(
            containerColor = scheme.primaryContainer,
            contentColor = scheme.onPrimaryContainer,
            disabledContainerColor = scheme.primaryContainer.copy(alpha = 0.5f),
            disabledContentColor = scheme.onPrimaryContainer.copy(alpha = 0.7f),
        )
        AppButtonType.DestructiveText -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = scheme.error,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = scheme.error.copy(alpha = 0.5f),
        )
    }

    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        contentPadding = PaddingValues(vertical = 16.dp),
        shape = RoundedCornerShape(14.dp),
        colors = colors,
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}
