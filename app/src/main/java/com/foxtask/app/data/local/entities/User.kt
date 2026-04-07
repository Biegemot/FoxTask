package com.foxtask.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: Int = 1,
    val level: Int = 1,
    val currentXp: Int = 0,
    val coins: Int = 0,
    val lastLevelUpDate: Long = System.currentTimeMillis()
)
