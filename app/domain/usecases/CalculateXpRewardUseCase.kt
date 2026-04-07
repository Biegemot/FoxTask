package com.foxtask.app.domain.usecases

class CalculateXpRewardUseCase {
    operator fun invoke(
        isHabit: Boolean,
        priority: Int = 3,
        streak: Int = 0,
        baseXp: Int = if (isHabit) 10 else 15
    ): Int {
        var reward = baseXp + (priority - 3) * 5
        if (isHabit && streak >= 7) {
            reward = (reward * 1.5).toInt()
        }
        return reward
    }
}
