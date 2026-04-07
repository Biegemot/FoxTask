package com.foxtask.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.foxtask.app.data.local.entities.Item

@Dao
interface ItemDao {
    @Query("SELECT * FROM items WHERE isActive = 1")
    suspend fun getAllActiveItems(): List<Item>

    @Query("SELECT * FROM items WHERE category = :category AND isActive = 1")
    suspend fun getItemsByCategory(category: String): List<Item>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItemById(id: Int): Item?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(vararg items: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Query("SELECT * FROM items WHERE tier = :tier AND isActive = 1")
    suspend fun getItemsByTier(tier: String): List<Item>
}
