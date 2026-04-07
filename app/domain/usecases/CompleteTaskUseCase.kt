package com.foxtask.app.domain.usecases

import com.foxtask.app.data.models.ItemTier
import com.foxtask.app.data.repository.FoxTaskRepository
import com.foxtask.app.domain.models.Reward
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CompleteTaskUseCase(
    private val repository: FoxTaskRepository,
    private val calculateLevelUseCase: CalculateLevelUseCase
) {
    suspend operator fun invoke(taskId: Int): Reward = withContext(Dispatchers.IO) {
        val task = repository.getTaskById(taskId) ?: return@withContext Reward(0, 0)
        if (task.isCompleted) return@withContext Reward(0, 0)

        // Рассчитываем награду
        var xp = task.xpReward
        var coins = task.coinReward

        // Бонус за приоритет если задача (не привычка)
        if (!task.isHabit && task.priority > 3) {
            val bonus = (task.priority - 3) * 5
            xp += bonus
        }

        // Помечаем задачу выполненной
        repository.setTaskCompleted(taskId, true)

        // Добавляем награду пользователю и пересчитываем уровень
        val user = repository.getUser()
        user?.let {
            val newXp = it.currentXp + xp
            val newCoins = it.coins + coins
            val (newLevel, _) = calculateLevelUseCase(newXp)
            repository.updateUser(newLevel, newXp, newCoins)
        }

        Reward(xp, coins)
    }
}
