package com.foxtask.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.foxtask.app.data.local.entities.HabitProgress

@Dao
interface HabitProgressDao {
    @Query("SELECT * FROM habit_progress WHERE habitId = :habitId ORDER BY date DESC")
    suspend fun getProgressForHabit(habitId: Int): List<HabitProgress>

    @Query("SELECT * FROM habit_progress")
    suspend fun getAllProgress(): List<HabitProgress>

    @Query("SELECT * FROM habit_progress WHERE habitId = :habitId AND date = :date")
    suspend fun getProgressForDate(habitId: Int, date: Long): HabitProgress?

    @Query("SELECT COUNT(*) FROM habit_progress WHERE habitId = :habitId AND completed = 1")
    suspend fun getTotalCompletedCount(habitId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: HabitProgress)

    @Update
    suspend fun updateProgress(progress: HabitProgress)

    @Query("DELETE FROM habit_progress WHERE habitId = :habitId AND date = :date")
    suspend fun deleteProgressForDate(habitId: Int, date: Long)
}
