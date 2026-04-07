package com.foxtask.app.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.foxtask.app.data.local.entities.Task
import com.foxtask.app.data.models.ItemTier
import com.foxtask.app.data.repository.FoxTaskRepository
import com.foxtask.app.domain.usecases.CalculateXpRewardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskEditViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = com.foxtask.app.di.ServiceLocator.getRepository()
    private val calculateXpRewardUseCase = CalculateXpRewardUseCase()

    private val _uiState = MutableStateFlow(TaskEditUiState())
    val uiState: StateFlow<TaskEditUiState> = _uiState.asStateFlow()

    fun loadTask(taskId: Int) {
        viewModelScope.launch {
            val task = repository.getTaskById(taskId)
            if (task != null) {
                _uiState.value = _uiState.value.copy(
                    title = task.title,
                    description = task.description ?: "",
                    priority = task.priority,
                    isHabit = task.isHabit,
                    xpReward = calculateXpRewardUseCase(
                        isHabit = task.isHabit,
                        priority = task.priority,
                        streak = task.streak
                    ),
                    coinReward = task.coinReward
                )
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
        updateRewards()
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun updatePriority(priority: Int) {
        _uiState.value = _uiState.value.copy(priority = priority.coerceIn(1, 5))
        updateRewards()
    }

    fun toggleHabit(isHabit: Boolean) {
        _uiState.value = _uiState.value.copy(isHabit = isHabit)
        updateRewards()
    }

    private fun updateRewards() {
        val state = _uiState.value
        val xp = calculateXpRewardUseCase(
            isHabit = state.isHabit,
            priority = state.priority,
            streak = 0 // new habit
        )
        val coins = if (state.isHabit) 5 else 5 + (state.priority - 3) * 2
        _uiState.value = state.copy(xpReward = xp, coinReward = coins)
    }

    fun saveTask(onComplete: (Boolean) -> Unit) {
        val state = _uiState.value
        if (state.title.isBlank()) {
            onComplete(false)
            return
        }

        viewModelScope.launch {
            try {
                val task = Task(
                    id = 0, // new task
                    title = state.title,
                    description = if (state.description.isBlank()) null else state.description,
                    dueDate = null,
                    priority = state.priority,
                    xpReward = state.xpReward,
                    coinReward = state.coinReward,
                    isCompleted = false,
                    isHabit = state.isHabit,
                    habitDays = 0,
                    streak = 0
                )
                repository.insertTask(task)
                onComplete(true)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }

    data class TaskEditUiState(
        val title: String = "",
        val description: String = "",
        val priority: Int = 3,
        val isHabit: Boolean = false,
        val xpReward: Int = 15,
        val coinReward: Int = 5
    )
}
