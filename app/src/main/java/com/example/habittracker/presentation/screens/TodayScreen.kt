package com.example.habittracker.presentation.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.habittracker.core.domain.model.HabitIcon
import com.example.habittracker.presentation.theme.HabitTrackerTheme
import com.example.habittracker.presentation.ui.components.AppCheckbox
import com.example.habittracker.presentation.ui.components.AppIconButton
import com.example.habittracker.presentation.ui.components.AppProgressBar
import com.example.habittracker.presentation.ui.components.IconBadge
import com.example.habittracker.presentation.ui.components.ScreenHeader
import com.example.habittracker.presentation.ui.icons.HabitTrackerIcons
import com.example.habittracker.presentation.ui.icons.addIcon
import com.example.habittracker.presentation.ui.icons.barChartIcon
import com.example.habittracker.presentation.ui.icons.starIcon
import com.example.habittracker.presentation.viewmodel.HabitItemUi
import com.example.habittracker.presentation.viewmodel.TodayViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TodayScreen(
    onStatsClick: () -> Unit,
    onAddHabit: () -> Unit,
    onEditHabit: (Long) -> Unit,
    viewModel: TodayViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TodayView(
        dateLabel = uiState.dateLabel,
        habits = uiState.habits,
        progressText = uiState.progressText,
        progress = uiState.progress,
        onStatsClick = onStatsClick,
        onAddHabit = onAddHabit,
        onToggleHabit = viewModel::onToggleHabit,
        onEditHabit = onEditHabit,
    )
}

@Composable
private fun TodayView(
    dateLabel: String,
    habits: List<HabitItemUi>,
    progressText: String,
    progress: Float,
    onStatsClick: () -> Unit,
    onAddHabit: () -> Unit,
    onToggleHabit: (Long) -> Unit,
    onEditHabit: (Long) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            ScreenHeader(
                title = "Today",
                trailing = {
                    AppIconButton(
                        onClick = onStatsClick,
                        background = MaterialTheme.colorScheme.surface,
                    ) {
                        Icon(
                            imageVector = HabitTrackerIcons.barChartIcon,
                            contentDescription = "Open statistics",
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                    }
                },
                modifier = Modifier.padding(top = 4.dp),
            )

            Text(
                text = dateLabel.ifBlank { ZonedDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d").withLocale(Locale.getDefault())) },
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Daily progress",
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    )
                    Text(
                        text = progressText.ifBlank { "${habits.count { it.completed }} / ${habits.size}" },
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.SemiBold,
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                AppProgressBar(progress = progress)
            }

            if (habits.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "No habits yet",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold,
                        ),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap + to add your first habit",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(habits, key = { it.id }) { habitItem ->
                        HabitListItem(
                            item = habitItem,
                            onToggle = { onToggleHabit(habitItem.id) },
                            onEdit = { onEditHabit(habitItem.id) },
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { onAddHabit() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = HabitTrackerIcons.addIcon,
                    contentDescription = "Create new habit",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

@Composable
private fun HabitListItem(
    item: HabitItemUi,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onEdit() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconBadge(
            icon = {
                Icon(
                    imageVector = HabitTrackerIcons.iconFor(item.icon),
                    contentDescription = item.name,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(22.dp),
                )
            },
            size = 42.dp,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
            )
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (item.currentStreak > 0) {
                    Icon(
                        imageVector = HabitTrackerIcons.starIcon,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(11.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${item.currentStreak} day streak",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 11.sp),
                        color = Color(0xFFF59E0B),
                    )
                } else {
                    Text(
                        text = "No streak yet",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        AppCheckbox(
            checked = item.completed,
            onCheckedChange = onToggle,
            modifier = Modifier.size(30.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F15)
@Composable
private fun TodayScreenPreview() {
    HabitTrackerTheme {
        TodayView(
            dateLabel = "Wednesday, September 16",
            habits = listOf(
                HabitItemUi(
                    id = 1L,
                    name = "Morning Run",
                    icon = HabitIcon.RUN,
                    completed = true,
                    currentStreak = 4,
                ),
                HabitItemUi(
                    id = 2L,
                    name = "Read 30min",
                    icon = HabitIcon.READ,
                    completed = false,
                    currentStreak = 2,
                ),
                HabitItemUi(
                    id = 3L,
                    name = "Drink Water",
                    icon = HabitIcon.WATER,
                    completed = true,
                    currentStreak = 7,
                ),
            ),
            progressText = "2 / 3",
            progress = 0.67f,
            onStatsClick = {},
            onAddHabit = {},
            onToggleHabit = {},
            onEditHabit = {},
        )
    }
}
