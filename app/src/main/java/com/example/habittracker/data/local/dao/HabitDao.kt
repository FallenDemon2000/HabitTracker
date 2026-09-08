package com.example.habittracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.habittracker.data.local.HabitEntity
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
     * SQLite `%w` is Sunday=0 through Saturday=6, matching [weekdayBit].
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
