package com.foxtask.app.data.local.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import com.foxtask.app.data.local.entities.Inventory
import com.foxtask.app.data.local.entities.Item

data class InventoryWithItem(
    @Embedded val inventory: Inventory,
    @Relation(parentColumn = "itemId", entityColumn = "id")
    val item: Item
)

@Dao
interface InventoryDao {
    @Query("SELECT * FROM inventory WHERE userId = :userId")
    suspend fun getInventoryForUser(userId: Int): List<Inventory>

    @Query("SELECT * FROM inventory WHERE userId = :userId AND isEquipped = 1")
    suspend fun getEquippedItems(userId: Int): List<Inventory>

    @Query("SELECT * FROM inventory WHERE userId = :userId AND itemId = :itemId")
    suspend fun getInventoryItem(userId: Int, itemId: Int): Inventory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryItem(inventory: Inventory)

    @Update
    suspend fun updateInventoryItem(inventory: Inventory)

    @Query("UPDATE inventory SET isEquipped = 0 WHERE userId = :userId")
    suspend fun unequipAllForUser(userId: Int)

    @Query("UPDATE inventory SET isEquipped = :equipped WHERE id = :inventoryId")
    suspend fun setEquipped(inventoryId: Int, equipped: Boolean)

    @Query("UPDATE inventory SET isEquipped = 1 WHERE id = :inventoryId")
    suspend fun equipItem(inventoryId: Int)

    @Query("UPDATE inventory SET isEquipped = 0 WHERE id = :inventoryId")
    suspend fun unequipItem(inventoryId: Int)

    @Transaction
    @Query("SELECT * FROM inventory WHERE userId = :userId")
    suspend fun getInventoryWithItems(userId: Int): List<InventoryWithItem>
}
