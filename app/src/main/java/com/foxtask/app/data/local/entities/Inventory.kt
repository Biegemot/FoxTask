package com.foxtask.app.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "inventory",
    indices = [
        Index(value = ["userId"]),
        Index(value = ["userId", "itemId"], unique = true),
        Index(value = ["isEquipped"])
    ]
)
data class Inventory(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val userId: Int,
    val itemId: Int,
    val purchaseDate: Long,
    val isEquipped: Boolean = false
)
