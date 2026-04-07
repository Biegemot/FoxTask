package com.foxtask.app.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.foxtask.app.data.local.entities.Task
import com.foxtask.app.domain.usecases.CompleteTaskUseCase
import com.foxtask.app.domain.usecases.CompleteHabitUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TasksViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = com.foxtask.app.di.ServiceLocator.getRepository()
    private val completeTaskUseCase = com.foxtask.app.di.ServiceLocator.getCompleteTaskUseCase()
    private val completeHabitUseCase = com.foxtask.app.di.ServiceLocator.getCompleteHabitUseCase()

    private val _tasksState = MutableStateFlow(TasksUiState())
    val tasksState: StateFlow<TasksUiState> = _tasksState.asStateFlow()

    init {
        loadTasks()
        loadHabits()
    }

    fun refresh() {
        loadTasks()
        loadHabits()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            val tasks = repository.getAllTasks()
            _tasksState.value = _tasksState.value.copy(tasks = tasks)
        }
    }

    private fun loadHabits() {
        viewModelScope.launch {
            val habits = repository.getAllHabits()
            _tasksState.value = _tasksState.value.copy(habits = habits)
        }
    }

    fun onTaskComplete(taskId: Int) {
        viewModelScope.launch {
            completeTaskUseCase.invoke(taskId)
            loadTasks()
        }
    }

    fun onHabitComplete(habitId: Int) {
        viewModelScope.launch {
            completeHabitUseCase.invoke(habitId)
            loadHabits()
            loadTasks() // also update user stats on main
        }
    }

    data class TasksUiState(
        val tasks: List<Task> = emptyList(),
        val habits: List<Task> = emptyList()
    )
}
