package com.example.habittracker

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.habittracker.presentation.screens.HabitEditorMode
import com.example.habittracker.presentation.screens.HabitEditorScreen
import com.example.habittracker.presentation.screens.Screens
import com.example.habittracker.presentation.screens.StatsScreen
import com.example.habittracker.presentation.screens.TodayScreen

@Composable
fun HabitTrackerApp() {
    val navController = rememberNavController()
    Scaffold { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val onNavigate: (Screens) -> Unit = { screen -> navController.navigate(screen) }
    val onBackClick: () -> Unit = { navController.popBackStack() }

    NavHost(
        navController = navController,
        startDestination = Screens.Today,
        modifier = modifier,
    ) {
        composable<Screens.Today> {
            TodayScreen(
                onStatsClick = { onNavigate(Screens.Statistics) },
                onAddHabit = { onNavigate(Screens.CreateHabit) },
                onEditHabit = { habitId -> onNavigate(Screens.EditHabit(habitId)) },
            )
        }

        composable<Screens.Statistics> {
            StatsScreen(onBack = onBackClick)
        }

        composable<Screens.CreateHabit> {
            HabitEditorScreen(
                mode = HabitEditorMode.CREATE,
                habitId = null,
                onBack = onBackClick,
            )
        }

        composable<Screens.EditHabit> { backStackEntry ->
            val habitId = backStackEntry.toRoute<Screens.EditHabit>().habitId
            HabitEditorScreen(
                mode = HabitEditorMode.EDIT,
                habitId = habitId,
                onBack = onBackClick,
            )
        }
    }
}
