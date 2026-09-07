package com.example.habittracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY id ASC")
    fun observeAll(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :habitId")
    fun observeById(habitId: Long): Flow<HabitEntity?>

    @Query("SELECT * FROM habits WHERE id = :habitId")
    suspend fun getById(habitId: Long): HabitEntity?

    /**
     * SQLite `%w` is Sunday=0 through Saturday=6, matching [com.example.habittracker.core.domain.model.weekdayBit].
     */
    @Query(
        """
        SELECT * FROM habits
        WHERE creation_date <= :date
          AND (weekday_mask & (1 << CAST(strftime('%w', :date) AS INTEGER))) != 0
        ORDER BY id ASC
        """,
    )
    fun observeScheduledForDate(date: LocalDate): Flow<List<HabitEntity>>

    @Insert
    suspend fun insert(habit: HabitEntity): Long

    @Query(
        """
        UPDATE habits
        SET name = :name, icon_id = :iconId, weekday_mask = :weekdayMask
        WHERE id = :habitId
        """,
    )
    suspend fun updateDefinition(
        habitId: Long,
        name: String,
        iconId: String,
        weekdayMask: Int,
    ): Int

    @Query("DELETE FROM habits WHERE id = :habitId")
    suspend fun deleteById(habitId: Long): Int
}

@Dao
interface HabitCompletionDao {
    @Query("SELECT * FROM habit_completions ORDER BY date ASC, habit_id ASC")
    fun observeAll(): Flow<List<HabitCompletionEntity>>

    @Query(
        """
        SELECT * FROM habit_completions
        WHERE date BETWEEN :from AND :to
        ORDER BY date ASC, habit_id ASC
        """,
    )
    fun observeForDateRange(from: LocalDate, to: LocalDate): Flow<List<HabitCompletionEntity>>

    @Query(
        """
        SELECT EXISTS(
            SELECT 1 FROM habit_completions
            WHERE habit_id = :habitId AND date = :date
        )
        """,
    )
    suspend fun exists(habitId: Long, date: LocalDate): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(completion: HabitCompletionEntity): Long

    @Delete
    suspend fun delete(completion: HabitCompletionEntity): Int
}
