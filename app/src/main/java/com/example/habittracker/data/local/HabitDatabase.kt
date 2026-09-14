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
 * Version 2 stores date-times as epoch milliseconds at the Room boundary.
 *
 * Streaks, percentages, active counts, and heatmap values are deliberately derived
 * from these rows. Future schema changes must use explicit migrations; destructive
 * fallback is intentionally not enabled.
 */
@Database(
    entities = [HabitEntity::class, HabitCompletionEntity::class],
    version = 2,
    exportSchema = true,
)
@TypeConverters(ZonedDateTimeConverters::class)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao

    abstract fun habitCompletionDao(): HabitCompletionDao

    companion object {
        fun create(context: Context): HabitDatabase {
            return Room.databaseBuilder(context, HabitDatabase::class.java, DATABASE_NAME)
                .addCallback(ValidationTriggers)
                .addMigrations(Migration1To2)
                .build()
        }

        private val Migration1To2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TRIGGER IF EXISTS habits_validate_insert")
                db.execSQL("DROP TRIGGER IF EXISTS habits_validate_update")
                db.execSQL("DROP TRIGGER IF EXISTS completions_validate_insert")
                db.execSQL("DROP INDEX IF EXISTS index_habit_completions_date_habit_id")
                db.execSQL("ALTER TABLE habits RENAME TO habits_v1")
                db.execSQL("ALTER TABLE habit_completions RENAME TO habit_completions_v1")
                db.execSQL(
                    """
                    CREATE TABLE habits (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        icon_id TEXT NOT NULL,
                        weekday_mask INTEGER NOT NULL,
                        creation_date INTEGER NOT NULL
                    )
                    """,
                )
                db.execSQL(
                    """
                    CREATE TABLE habit_completions (
                        habit_id INTEGER NOT NULL,
                        date INTEGER NOT NULL,
                        PRIMARY KEY(habit_id, date),
                        FOREIGN KEY(habit_id) REFERENCES habits(id) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """,
                )
                db.execSQL("CREATE INDEX index_habit_completions_date_habit_id ON habit_completions(date, habit_id)")
                db.execSQL(
                    """
                    INSERT INTO habits(id, name, icon_id, weekday_mask, creation_date)
                    SELECT id, name, icon_id, weekday_mask,
                        CAST(strftime('%s', creation_date) AS INTEGER) * 1000
                    FROM habits_v1
                    """,
                )
                db.execSQL(
                    """
                    INSERT INTO habit_completions(habit_id, date)
                    SELECT habit_id, CAST(strftime('%s', date) AS INTEGER) * 1000
                    FROM habit_completions_v1
                    """,
                )
                db.execSQL("DROP TABLE habit_completions_v1")
                db.execSQL("DROP TABLE habits_v1")
            }
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
                SELECT RAISE(ABORT, 'creation date must be valid and not future dated')
                WHERE date(NEW.creation_date / 1000, 'unixepoch', 'localtime') > date('now', 'localtime');
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
                SELECT RAISE(ABORT, 'creation date must be valid and not future dated')
                WHERE date(NEW.creation_date / 1000, 'unixepoch', 'localtime') > date('now', 'localtime');
            END
            """,
        )
        db.execSQL(
            """
            CREATE TRIGGER IF NOT EXISTS completions_validate_insert
            BEFORE INSERT ON habit_completions
            BEGIN
                SELECT RAISE(ABORT, 'completion date must be valid and not future dated')
                WHERE date(NEW.date / 1000, 'unixepoch', 'localtime') > date('now', 'localtime');
                SELECT RAISE(ABORT, 'completion habit does not exist')
                WHERE NOT EXISTS (SELECT 1 FROM habits WHERE id = NEW.habit_id);
                SELECT RAISE(ABORT, 'completion date is before habit creation')
                WHERE date(NEW.date / 1000, 'unixepoch', 'localtime') <
                    date((SELECT creation_date FROM habits WHERE id = NEW.habit_id) / 1000, 'unixepoch', 'localtime');
                SELECT RAISE(ABORT, 'completion date is not scheduled')
                WHERE (
                    (SELECT weekday_mask FROM habits WHERE id = NEW.habit_id)
                    & (1 << CAST(strftime('%w', NEW.date / 1000, 'unixepoch', 'localtime') AS INTEGER))
                ) = 0;
            END
            """,
        )
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("PRAGMA foreign_keys = ON")
        db.execSQL("DROP TRIGGER IF EXISTS habits_validate_insert")
        db.execSQL("DROP TRIGGER IF EXISTS habits_validate_update")
        db.execSQL("DROP TRIGGER IF EXISTS completions_validate_insert")
        onCreate(db)
    }
}
