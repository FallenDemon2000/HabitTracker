package com.example.habittracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.habittracker.data.local.HabitCompletionEntity
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

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
    fun observeForDateRange(from: ZonedDateTime, to: ZonedDateTime): Flow<List<HabitCompletionEntity>>

    @Query(
        """
        SELECT EXISTS(
            SELECT 1 FROM habit_completions
            WHERE habit_id = :habitId AND date = :date
        )
        """,
    )
    suspend fun exists(habitId: Long, date: ZonedDateTime): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(completion: HabitCompletionEntity): Long

    @Delete
    suspend fun delete(completion: HabitCompletionEntity): Int
}
