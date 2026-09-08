package com.example.habittracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.habittracker.data.local.dao.HabitCompletionDao
import com.example.habittracker.data.local.dao.HabitDao

/**
 * Version 1 contains only editable habit definitions and binary dated completions.
 *
 * Streaks, percentages, active counts, and heatmap values are deliberately derived
 * from these rows. Future schema changes must use explicit migrations; destructive
 * fallback is intentionally not enabled.
 */
@Database(
    entities = [HabitEntity::class, HabitCompletionEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(LocalDateConverters::class)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao

    abstract fun habitCompletionDao(): HabitCompletionDao

    companion object {
        fun create(context: Context): HabitDatabase {
            return Room.databaseBuilder(context, HabitDatabase::class.java, DATABASE_NAME)
                .addCallback(ValidationTriggers)
                .build()
        }

        private const val DATABASE_NAME = "habit_tracker.db"
    }
}

private object ValidationTriggers : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        db.execSQL(
            """
            CREATE TRIGGER IF NOT EXISTS habits_validate_insert
            BEFORE INSERT ON habits
            BEGIN
                SELECT RAISE(ABORT, 'habit name must be trimmed, nonblank, and at most 50 characters')
                WHERE NEW.name != trim(NEW.name)
                   OR length(trim(NEW.name)) = 0
                   OR length(NEW.name) > 50;
                SELECT RAISE(ABORT, 'unknown habit icon')
                WHERE NEW.icon_id NOT IN (
                    'run', 'read', 'water', 'meditate', 'sleep', 'code', 'music', 'cook',
                    'journal', 'gym', 'yoga', 'walk', 'cycle', 'study', 'no_phone',
                    'vitamins', 'language', 'gratitude', 'health', 'organize'
                );
                SELECT RAISE(ABORT, 'weekday mask must be between 1 and 127')
                WHERE NEW.weekday_mask NOT BETWEEN 1 AND 127;
                SELECT RAISE(ABORT, 'creation date must be ISO and not future dated')
                WHERE strftime('%Y-%m-%d', NEW.creation_date) IS NULL
                   OR strftime('%Y-%m-%d', NEW.creation_date) != NEW.creation_date
                   OR NEW.creation_date > date('now', 'localtime');
            END
            """,
        )
        db.execSQL(
            """
            CREATE TRIGGER IF NOT EXISTS habits_validate_update
            BEFORE UPDATE OF name, icon_id, weekday_mask, creation_date ON habits
            BEGIN
                SELECT RAISE(ABORT, 'habit name must be trimmed, nonblank, and at most 50 characters')
                WHERE NEW.name != trim(NEW.name)
                   OR length(trim(NEW.name)) = 0
                   OR length(NEW.name) > 50;
                SELECT RAISE(ABORT, 'unknown habit icon')
                WHERE NEW.icon_id NOT IN (
                    'run', 'read', 'water', 'meditate', 'sleep', 'code', 'music', 'cook',
                    'journal', 'gym', 'yoga', 'walk', 'cycle', 'study', 'no_phone',
                    'vitamins', 'language', 'gratitude', 'health', 'organize'
                );
                SELECT RAISE(ABORT, 'weekday mask must be between 1 and 127')
                WHERE NEW.weekday_mask NOT BETWEEN 1 AND 127;
                SELECT RAISE(ABORT, 'creation date is immutable')
                WHERE NEW.creation_date != OLD.creation_date;
                SELECT RAISE(ABORT, 'creation date must be ISO and not future dated')
                WHERE strftime('%Y-%m-%d', NEW.creation_date) IS NULL
                   OR strftime('%Y-%m-%d', NEW.creation_date) != NEW.creation_date
                   OR NEW.creation_date > date('now', 'localtime');
            END
            """,
        )
        db.execSQL(
            """
            CREATE TRIGGER IF NOT EXISTS completions_validate_insert
            BEFORE INSERT ON habit_completions
            BEGIN
                SELECT RAISE(ABORT, 'completion date must be ISO and not future dated')
                WHERE strftime('%Y-%m-%d', NEW.date) IS NULL
                   OR strftime('%Y-%m-%d', NEW.date) != NEW.date
                   OR NEW.date > date('now', 'localtime');
                SELECT RAISE(ABORT, 'completion habit does not exist')
                WHERE NOT EXISTS (SELECT 1 FROM habits WHERE id = NEW.habit_id);
                SELECT RAISE(ABORT, 'completion date is before habit creation')
                WHERE NEW.date < (SELECT creation_date FROM habits WHERE id = NEW.habit_id);
                SELECT RAISE(ABORT, 'completion date is not scheduled')
                WHERE (
                    (SELECT weekday_mask FROM habits WHERE id = NEW.habit_id)
                    & (1 << CAST(strftime('%w', NEW.date) AS INTEGER))
                ) = 0;
            END
            """,
        )
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("PRAGMA foreign_keys = ON")
    }
}
