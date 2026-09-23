package com.example.habittracker.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.habittracker.core.domain.model.HabitIcon
import com.example.habittracker.core.domain.model.HabitId
import com.example.habittracker.presentation.ui.components.AppButton
import com.example.habittracker.presentation.ui.components.AppButtonType
import com.example.habittracker.presentation.ui.components.AppDaySelector
import com.example.habittracker.presentation.ui.components.AppIconButton
import com.example.habittracker.presentation.ui.components.AppTextField
import com.example.habittracker.presentation.ui.components.IconBadge
import com.example.habittracker.presentation.ui.components.ScreenHeader
import com.example.habittracker.presentation.theme.HabitTrackerTheme
import com.example.habittracker.presentation.ui.icons.HabitTrackerIcons
import com.example.habittracker.presentation.ui.icons.arrowBackIcon
import com.example.habittracker.presentation.viewmodel.HabitEditorViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material3.MaterialTheme
import java.time.DayOfWeek
import java.util.Locale

enum class HabitEditorMode { CREATE, EDIT }

@Composable
fun HabitEditorScreen(
    mode: HabitEditorMode,
    habitId: Long?,
    onBack: () -> Unit,
    viewModel: HabitEditorViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.loadHabit(habitId)

    HabitEditorView(
        mode = mode,
        name = uiState.name,
        icon = uiState.icon,
        selectedDays = uiState.selectedDays,
        onNameChange = { value -> viewModel.onNameChanged(value) },
        onIconChange = { value -> viewModel.onIconChanged(value) },
        onDaysChange = { value -> viewModel.onDaysChanged(value) },
        onBack = onBack,
        onSave = { name, icon, days ->
            viewModel.saveHabit(mode, name, icon, days)
            onBack()
        },
        onDelete = {
            val resolvedId = habitId ?: return@HabitEditorView
            viewModel.deleteHabit(HabitId(resolvedId))
            onBack()
        },
    )
}

@Composable
private fun HabitEditorView(
    mode: HabitEditorMode,
    name: String,
    icon: HabitIcon,
    selectedDays: Set<DayOfWeek>,
    onNameChange: (String) -> Unit,
    onIconChange: (HabitIcon) -> Unit,
    onDaysChange: (Set<DayOfWeek>) -> Unit,
    onBack: () -> Unit,
    onSave: (String, HabitIcon, Set<DayOfWeek>) -> Unit,
    onDelete: (() -> Unit),
) {
    var nameValue by remember(name) { mutableStateOf(name) }
    var activeIcon by remember(icon) { mutableStateOf(icon) }
    var expandedPicker by remember { mutableStateOf(mode == HabitEditorMode.CREATE) }
    var days by remember(selectedDays) { mutableStateOf(selectedDays) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        ScreenHeader(
            title = if (mode == HabitEditorMode.CREATE) "New Habit" else "Edit Habit",
            leading = {
                AppIconButton(onClick = onBack, background = MaterialTheme.colorScheme.surface) {
                    Icon(
                        imageVector = HabitTrackerIcons.arrowBackIcon,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            },
            modifier = Modifier.padding(top = 4.dp),
        )

        Spacer(modifier = Modifier.height(22.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().clickable { expandedPicker = !expandedPicker },
        ) {
            IconBadge(
                icon = {
                    Icon(
                        imageVector = HabitTrackerIcons.iconFor(activeIcon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(28.dp),
                    )
                },
                size = 72.dp,
                shape = RoundedCornerShape(20.dp),
                hasBorder = true,
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = "Tap to change icon",
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary),
            )
        }

        if (expandedPicker) {
            Spacer(modifier = Modifier.height(20.dp))
            AppButton(
                text = "Choose Icon",
                onClick = { expandedPicker = false },
                type = AppButtonType.Secondary,
            )
            Spacer(modifier = Modifier.height(14.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(HabitIcon.entries.toList()) { icon ->
                    val selected = activeIcon == icon
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                width = if (selected) 2.dp else 0.dp,
                                color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = RoundedCornerShape(12.dp),
                            )
                            .clickable {
                                activeIcon = icon
                                onIconChange(icon)
                                expandedPicker = false
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = HabitTrackerIcons.iconFor(icon),
                            contentDescription = icon.name.lowercase(Locale.getDefault()),
                            tint = if (selected) Color.White else MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(22.dp),
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        AppTextField(
            value = nameValue,
            onValueChange = {
                nameValue = it
                onNameChange(it)
            },
            label = "Name",
            placeholder = "Habit name",
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Repeat on".uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 10.dp),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val dayOrder = listOf(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY,
            )
            dayOrder.forEach { day ->
                AppDaySelector(
                    label = day.name.take(1).uppercase(Locale.getDefault()),
                    selected = days.contains(day),
                    onClick = {
                        days = if (days.contains(day)) days - day else days + day
                        onDaysChange(days)
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        AppButton(
            text = if (mode == HabitEditorMode.CREATE) "Save Habit" else "Save Changes",
            onClick = {
                if (nameValue.isBlank() || days.isEmpty()) {
                    return@AppButton
                }
                onSave(nameValue.trim(), activeIcon, days)
            },
            type = AppButtonType.Primary,
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (mode == HabitEditorMode.CREATE) {
            Text(
                text = "Discard Habit",
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onBack() },
            )
        } else {
            Text(
                text = "Delete Habit",
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDeleteDialog = true },
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete habit?") },
            text = { Text("This will remove the habit and all of its completion records.") },
            confirmButton = {
                Text(
                    text = "Delete",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.clickable {
                        showDeleteDialog = false
                        onDelete()
                    },
                )
            },
            dismissButton = {
                Text(
                    text = "Cancel",
                    modifier = Modifier.clickable { showDeleteDialog = false },
                )
            },
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF0F0F15)
@Composable
private fun CreateHabitPreview() {
    HabitTrackerTheme {
        HabitEditorView(
            mode = HabitEditorMode.CREATE,
            name = "Morning Run",
            icon = HabitIcon.RUN,
            selectedDays = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            onNameChange = {},
            onIconChange = {},
            onDaysChange = {},
            onBack = {},
            onSave = { _, _, _ -> },
            onDelete = {},
        )
    }
}
