package com.foxtask.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.foxtask.app.data.local.entities.User

import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getUser(): User?

    @Query("SELECT * FROM user LIMIT 1")
    fun getUserStream(): Flow<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("UPDATE user SET level = :level, currentXp = :currentXp, coins = :coins WHERE id = 1")
    suspend fun updateUser(level: Int, currentXp: Int, coins: Int)

    @Query("UPDATE user SET coins = coins + :addedCoins WHERE id = 1")
    suspend fun addCoins(addedCoins: Int)

    @Query("UPDATE user SET currentXp = :xp WHERE id = 1")
    suspend fun setCurrentXp(xp: Int)

    @Query("UPDATE user SET level = :level, currentXp = :currentXp WHERE id = 1")
    suspend fun setLevelAndXp(level: Int, currentXp: Int)
}
