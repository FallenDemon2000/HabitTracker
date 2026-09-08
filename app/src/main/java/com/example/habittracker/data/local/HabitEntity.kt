package com.example.habittracker.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "icon_id")
    val iconId: String,
    @ColumnInfo(name = "weekday_mask")
    val weekdayMask: Int,
    @ColumnInfo(name = "creation_date")
    val creationDate: LocalDate,
)
