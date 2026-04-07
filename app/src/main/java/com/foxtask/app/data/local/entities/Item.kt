package com.foxtask.app.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.foxtask.app.data.models.ItemCategory
import com.foxtask.app.data.models.ItemTier

@Entity(
    tableName = "items",
    indices = [
        Index(value = ["category"]),
        Index(value = ["tier"]),
        Index(value = ["isActive"])
    ]
)
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val description: String,
    val category: ItemCategory,
    val tier: ItemTier,
    val cost: Int,
    val drawableResName: String,
    val isPremium: Boolean = false,
    val lottieAnimationRes: String? = null,
    val requiredLevel: Int = 1,
    val isActive: Boolean = true
)
