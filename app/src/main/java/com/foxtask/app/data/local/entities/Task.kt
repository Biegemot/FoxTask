package com.foxtask.app.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    indices = [
        Index(value = ["isHabit"]),
        Index(value = ["isCompleted"]),
        Index(value = ["isHabit", "isCompleted"]),
        Index(value = ["dueDate"])
    ]
)
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String?,
    val dueDate: Long?, // null для привычек без конкретной даты
    val priority: Int = 3, // 1-5
    val xpReward: Int,
    val coinReward: Int,
    val isCompleted: Boolean = false,
    val isHabit: Boolean = false,
    val habitDays: Int = 0, // bitmask для дней недели (7 бит)
    val reminderEnabled: Boolean = false,
    val reminderTime: String? = "09:00",
    val streak: Int = 0,
    val lastCompletedDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
