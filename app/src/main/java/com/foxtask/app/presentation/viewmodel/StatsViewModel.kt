package com.foxtask.app.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.foxtask.app.data.repository.FoxTaskRepository
import com.foxtask.app.domain.models.Statistics
import com.foxtask.app.domain.usecases.GetStatisticsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val getStatisticsUseCase = com.foxtask.app.di.ServiceLocator.getGetStatisticsUseCase()

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    fun refresh() {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            val stats = getStatisticsUseCase.invoke()
            _uiState.value = _uiState.value.copy(statistics = stats)
        }
    }

    data class StatsUiState(
        val statistics: Statistics? = null
    )
}
