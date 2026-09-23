package com.example.habittracker.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Icon
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
import com.example.habittracker.core.domain.model.HabitStatistics
import com.example.habittracker.core.domain.model.HabitStreak
import com.example.habittracker.core.domain.model.HeatmapCell
import com.example.habittracker.core.domain.model.HabitId
import com.example.habittracker.core.domain.model.TodayProgress
import com.example.habittracker.presentation.ui.components.AppCard
import com.example.habittracker.presentation.ui.components.AppIconButton
import com.example.habittracker.presentation.ui.components.ScreenHeader
import com.example.habittracker.presentation.theme.HabitTrackerTheme
import com.example.habittracker.presentation.viewmodel.StatsViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material3.MaterialTheme
import java.time.ZonedDateTime

@Composable
fun StatsScreen(
    onBack: () -> Unit,
    viewModel: StatsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    StatsView(
        currentWeekPercentage = uiState.currentWeekPercentage,
        bestStreak = uiState.bestStreak,
        activeCount = uiState.activeCount,
        heatmap = uiState.heatmap,
        streaks = uiState.streaks,
        onBack = onBack,
    )
}

@Composable
private fun StatsView(
    currentWeekPercentage: Int,
    bestStreak: Int,
    activeCount: Int,
    heatmap: List<HeatmapCell>,
    streaks: List<HabitStreak>,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        ScreenHeader(
            title = "Statistics",
            leading = {
                AppIconButton(
                    onClick = onBack,
                    background = MaterialTheme.colorScheme.surface,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back to Today",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            },
            modifier = Modifier.padding(top = 4.dp),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            SummaryCard(
                label = "This Week",
                value = "${currentWeekPercentage}%",
                valueColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f),
            )
            SummaryCard(
                label = "Best Streak",
                value = "$bestStreak",
                valueColor = Color(0xFF34D399),
                modifier = Modifier.weight(1f),
            )
            SummaryCard(
                label = "Active",
                value = "$activeCount",
                valueColor = Color(0xFFF472B6),
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Activity",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(10.dp))

        AppCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Spacer(modifier = Modifier.width(28.dp))
                    listOf("M", "T", "W", "T", "F", "S", "S").forEach {
                        Box(
                            modifier = Modifier.width(32.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 9.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                heatmap.chunked(7).forEachIndexed { rowIndex, values ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "W${rowIndex + 1}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 9.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.width(24.dp),
                        )
                        values.forEach { cell ->
                            ActivityHeatCell(
                                cell = cell,
                                modifier = Modifier.width(32.dp).height(32.dp),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Less",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 9.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    repeat(5) { index ->
                        Box(
                            modifier = Modifier
                                .width(14.dp)
                                .height(14.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(activityLegendColor(index))
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "More",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 9.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Streaks",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(streaks, key = { it.habitId.value }) { streak ->
                HabitStreakRow(
                    habitName = streak.name,
                    icon = streak.icon,
                    current = streak.current,
                    best = streak.best,
                )
            }
        }
    }
}

@Composable
private fun ActivityHeatCell(
    cell: HeatmapCell,
    modifier: Modifier = Modifier,
) {
    val color = when {
        cell.isFuture -> Color(0xFF252540)
        cell.percentage == null -> Color(0xFF252540)
        cell.percentage <= 0 -> Color(0xFF252540)
        cell.percentage <= 25 -> Color(0xFF6C63FF).copy(alpha = 0.3f)
        cell.percentage <= 50 -> Color(0xFF6C63FF).copy(alpha = 0.6f)
        cell.percentage <= 75 -> Color(0xFF6C63FF).copy(alpha = 0.85f)
        else -> Color(0xFF6C63FF)
    }
    val isToday = cell.date.toLocalDate().isEqual(java.time.LocalDate.now())
    val isFutureCell = cell.isFuture
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color)
            .then(
                if (isFutureCell) Modifier.border(1.dp, Color(0xFF333350), RoundedCornerShape(6.dp))
                else if (isToday) Modifier.border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(6.dp))
                else Modifier
            )
    )
}

private fun activityLegendColor(index: Int): Color = when (index) {
    0 -> Color(0xFF252540)
    1 -> Color(0xFF6C63FF).copy(alpha = 0.3f)
    2 -> Color(0xFF6C63FF).copy(alpha = 0.6f)
    3 -> Color(0xFF6C63FF).copy(alpha = 0.85f)
    else -> Color(0xFF6C63FF)
}

@Composable
private fun SummaryCard(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
) {
    AppCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.displayLarge,
                color = valueColor,
            )
        }
    }
}

@Composable
private fun HabitStreakRow(
    habitName: String,
    icon: HabitIcon,
    current: Int,
    best: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = when (icon) {
                    HabitIcon.RUN -> androidx.compose.material.icons.Icons.Outlined.BarChart
                    HabitIcon.READ -> androidx.compose.material.icons.Icons.Outlined.MenuBook
                    HabitIcon.WATER -> androidx.compose.material.icons.Icons.Outlined.WaterDrop
                    HabitIcon.MEDITATE -> androidx.compose.material.icons.Icons.Outlined.SelfImprovement
                    HabitIcon.SLEEP -> androidx.compose.material.icons.Icons.Outlined.Star
                    HabitIcon.CODE -> androidx.compose.material.icons.Icons.Outlined.Bolt
                    HabitIcon.MUSIC -> androidx.compose.material.icons.Icons.Outlined.MusicNote
                    HabitIcon.COOK -> androidx.compose.material.icons.Icons.Outlined.Star
                    HabitIcon.JOURNAL -> androidx.compose.material.icons.Icons.Outlined.Star
                    HabitIcon.GYM -> androidx.compose.material.icons.Icons.Outlined.Star
                    HabitIcon.YOGA -> androidx.compose.material.icons.Icons.Outlined.SelfImprovement
                    HabitIcon.WALK -> androidx.compose.material.icons.Icons.Outlined.DirectionsWalk
                    HabitIcon.CYCLE -> androidx.compose.material.icons.Icons.Outlined.Star
                    HabitIcon.STUDY -> androidx.compose.material.icons.Icons.Outlined.CalendarMonth
                    HabitIcon.NO_PHONE -> androidx.compose.material.icons.Icons.Outlined.Star
                    HabitIcon.VITAMINS -> androidx.compose.material.icons.Icons.Outlined.HealthAndSafety
                    HabitIcon.LANGUAGE -> androidx.compose.material.icons.Icons.Outlined.Star
                    HabitIcon.GRATITUDE -> androidx.compose.material.icons.Icons.Outlined.Star
                    HabitIcon.HEALTH -> androidx.compose.material.icons.Icons.Outlined.MedicalServices
                    HabitIcon.ORGANIZE -> androidx.compose.material.icons.Icons.Outlined.Star
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(18.dp),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = habitName,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = current.toString(),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                color = Color(0xFF34D399),
            )
            Text(
                text = "Best: $best",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F15)
@Composable
private fun StatsScreenPreview() {
    val previewValues = listOf(
        0, 25, 60, 100, 80, 15, 0,
        35, 0, 75, 90, 100, 30, 50,
        20, 55, 85, 100, 40, 0, 70,
        100, 45, 0, 90, 80, 25, 65,
    )
    val today = ZonedDateTime.now()
    val startDate = today.toLocalDate().with(java.time.DayOfWeek.MONDAY).minusWeeks(3)
    val previewHeatmap = previewValues.mapIndexed { index, value ->
        val date = startDate.plusDays(index.toLong())
        HeatmapCell(
            date = date.atStartOfDay(today.zone),
            completedScheduledHabits = if (value == 0) 0 else value / 25,
            scheduledHabits = 1,
            percentage = if (date.isAfter(today.toLocalDate())) null else value,
        )
    }
    val previewStreaks = listOf(
        HabitStreak(habitId = com.example.habittracker.core.domain.model.HabitId(1), name = "Morning Run", icon = HabitIcon.RUN, current = 12, best = 18),
        HabitStreak(habitId = com.example.habittracker.core.domain.model.HabitId(2), name = "Read 30 min", icon = HabitIcon.READ, current = 5, best = 10),
        HabitStreak(habitId = com.example.habittracker.core.domain.model.HabitId(3), name = "Drink Water", icon = HabitIcon.WATER, current = 3, best = 7),
    )

    HabitTrackerTheme {
        StatsView(
            currentWeekPercentage = 87,
            bestStreak = 12,
            activeCount = 7,
            heatmap = previewHeatmap,
            streaks = previewStreaks,
            onBack = {},
        )
    }
}
