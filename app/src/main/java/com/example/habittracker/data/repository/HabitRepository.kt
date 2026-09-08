package com.example.habittracker.data.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.room.withTransaction
import com.example.habittracker.core.domain.error.HabitError
import com.example.habittracker.core.domain.error.HabitResult
import com.example.habittracker.core.domain.model.Habit
import com.example.habittracker.core.domain.model.HabitCompletion
import com.example.habittracker.core.domain.model.HabitDraft
import com.example.habittracker.core.domain.model.HabitId
import com.example.habittracker.core.domain.model.HabitStatistics
import com.example.habittracker.core.domain.model.HabitUpdate
import com.example.habittracker.core.domain.model.TodayProgress
import com.example.habittracker.core.domain.statistics.HabitStatisticsCalculator
import com.example.habittracker.core.domain.validation.HabitValidator
import com.example.habittracker.data.local.HabitCompletionEntity
import com.example.habittracker.data.local.HabitDatabase
import com.example.habittracker.data.local.dao.HabitCompletionDao
import com.example.habittracker.data.local.dao.HabitDao
import com.example.habittracker.data.local.toCompletionDomain
import com.example.habittracker.data.local.toDomain
import com.example.habittracker.data.local.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate

/**
 * The only data-layer entry point for habits and completions.
 *
 * Reads expose Room-backed flows, while writes validate at the domain boundary.
 * Only known SQLite constraint failures become [HabitResult.Failure]; cancellation
 * and unexpected persistence exceptions are allowed to propagate.
 */
class HabitRepository(
    private val database: HabitDatabase,
    private val habitDao: HabitDao,
    private val completionDao: HabitCompletionDao,
    private val validator: HabitValidator,
    private val statisticsCalculator: HabitStatisticsCalculator,
) {
    fun observeHabits(): Flow<List<Habit>> {
        return habitDao.observeAll().map { it.toDomain() }
    }

    fun observeHabit(habitId: HabitId): Flow<Habit?> {
        return habitDao.observeById(habitId.value).map { it?.toDomain() }
    }

    fun observeScheduledHabits(date: LocalDate): Flow<List<Habit>> {
        return habitDao.observeScheduledForDate(date)
            .map { it.toDomain() }
    }

    fun observeToday(today: LocalDate): Flow<TodayProgress> {
        return combine(observeHabits(), observeCompletions()) { habits, completions ->
            statisticsCalculator.todayProgress(habits, completions, today)
        }
    }

    fun observeStatistics(today: LocalDate): Flow<HabitStatistics> {
        return combine(observeHabits(), observeCompletions()) { habits, completions ->
            statisticsCalculator.statistics(habits, completions, today)
        }
    }

    suspend fun createHabit(
        draft: HabitDraft,
        today: LocalDate = LocalDate.now(),
    ): HabitResult<Habit> {
        val normalized = draft.copy(name = validator.normalizeName(draft.name))
        validator.validateDraft(normalized, today)?.let { return HabitResult.Failure(it) }
        return try {
            val id = habitDao.insert(normalized.toEntity())
            if (id <= 0) {
                HabitResult.Failure(HabitError.PersistenceConstraint("create habit"))
            } else {
                HabitResult.Success(
                    Habit(
                        id = HabitId(id),
                        name = normalized.name,
                        iconId = normalized.iconId,
                        weekdayMask = normalized.weekdayMask,
                        creationDate = normalized.creationDate,
                    ),
                )
            }
        } catch (_: SQLiteConstraintException) {
            HabitResult.Failure(HabitError.PersistenceConstraint("create habit"))
        }
    }

    suspend fun updateHabit(
        habitId: HabitId,
        update: HabitUpdate,
    ): HabitResult<Habit> {
        val normalized = update.copy(name = validator.normalizeName(update.name))
        validator.validateUpdate(normalized)?.let { return HabitResult.Failure(it) }
        val existing = habitDao.getById(habitId.value)
            ?: return HabitResult.Failure(HabitError.NotFound(habitId))
        return try {
            val updatedRows = habitDao.updateDefinition(
                habitId = habitId.value,
                name = normalized.name,
                iconId = normalized.iconId.value,
                weekdayMask = normalized.weekdayMask.value,
            )
            if (updatedRows == 0) {
                HabitResult.Failure(HabitError.NotFound(habitId))
            } else {
                HabitResult.Success(
                    existing.toDomain().copy(
                        name = normalized.name,
                        iconId = normalized.iconId,
                        weekdayMask = normalized.weekdayMask,
                    ),
                )
            }
        } catch (_: SQLiteConstraintException) {
            HabitResult.Failure(HabitError.PersistenceConstraint("update habit"))
        }
    }

    /**
     * Toggles one valid local date atomically. Row existence is the completion state.
     */
    suspend fun toggleCompletion(
        habitId: HabitId,
        date: LocalDate,
        today: LocalDate = LocalDate.now(),
    ): HabitResult<Boolean> {
        return try {
            database.withTransaction {
                val habit = habitDao.getById(habitId.value)?.toDomain()
                    ?: return@withTransaction HabitResult.Failure(HabitError.NotFound(habitId))
                validator.validateCompletion(habit, date, today)?.let {
                    return@withTransaction HabitResult.Failure(it)
                }
                val completion = HabitCompletionEntity(habitId.value, date)
                if (completionDao.exists(habitId.value, date)) {
                    completionDao.delete(completion)
                    HabitResult.Success(false)
                } else {
                    val inserted = completionDao.insert(completion)
                    if (inserted == -1L) {
                        HabitResult.Failure(HabitError.PersistenceConstraint("toggle completion"))
                    } else {
                        HabitResult.Success(true)
                    }
                }
            }
        } catch (_: SQLiteConstraintException) {
            HabitResult.Failure(HabitError.PersistenceConstraint("toggle completion"))
        }
    }

    suspend fun deleteHabit(habitId: HabitId): HabitResult<Unit> {
        return try {
            database.withTransaction {
                if (habitDao.deleteById(habitId.value) == 0) {
                    HabitResult.Failure(HabitError.NotFound(habitId))
                } else {
                    HabitResult.Success(Unit)
                }
            }
        } catch (_: SQLiteConstraintException) {
            HabitResult.Failure(HabitError.PersistenceConstraint("delete habit"))
        }
    }

    private fun observeCompletions(): Flow<List<HabitCompletion>> {
        return completionDao.observeAll().map { entities ->
            entities.toCompletionDomain()
        }
    }
}
