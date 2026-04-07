package com.foxtask.app.data.local

import androidx.room.TypeConverter
import com.foxtask.app.data.models.ItemCategory
import com.foxtask.app.data.models.ItemTier
import com.foxtask.app.data.models.TaskPriority

class Converters {
    @TypeConverter
    fun fromItemCategory(category: ItemCategory): String = category.name

    @TypeConverter
    fun toItemCategory(category: String): ItemCategory = ItemCategory.valueOf(category)

    @TypeConverter
    fun fromItemTier(tier: ItemTier): String = tier.name

    @TypeConverter
    fun toItemTier(tier: String): ItemTier = ItemTier.valueOf(tier)

    @TypeConverter
    fun fromTaskPriority(priority: TaskPriority): Int = priority.value

    @TypeConverter
    fun toTaskPriority(value: Int): TaskPriority = TaskPriority.fromValue(value)
}
