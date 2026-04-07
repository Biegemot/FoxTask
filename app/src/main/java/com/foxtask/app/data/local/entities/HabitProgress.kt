package com.foxtask.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "habit_progress",
    primaryKeys = ["habitId", "date"]
)
data class HabitProgress(
    val habitId: Int,
    val date: Long, // epoch day (UTC)
    val completed: Boolean
)
