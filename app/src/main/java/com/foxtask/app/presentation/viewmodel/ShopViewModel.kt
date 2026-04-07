package com.foxtask.app.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.foxtask.app.data.local.entities.Inventory
import com.foxtask.app.data.local.entities.Outfit
import com.foxtask.app.data.models.*
import com.foxtask.app.data.repository.FoxTaskRepository
import com.foxtask.app.domain.usecases.EquipItemUseCase
import com.foxtask.app.domain.usecases.PurchaseItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShopViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = com.foxtask.app.di.ServiceLocator.getRepository()
    private val purchaseItemUseCase = com.foxtask.app.di.ServiceLocator.getPurchaseItemUseCase()
    private val equipItemUseCase = com.foxtask.app.di.ServiceLocator.getEquipItemUseCase()

    private val _uiState = MutableStateFlow(ShopUiState())
    val uiState: StateFlow<ShopUiState> = _uiState.asStateFlow()

    init {
        loadItems()
        loadInventory()
        loadOutfit()
        loadUser()
    }

    fun refresh() {
        loadItems()
        loadInventory()
        loadOutfit()
        loadUser()
    }

    fun loadItems() {
        viewModelScope.launch {
            val items = repository.getAllActiveItems()
            _uiState.value = _uiState.value.copy(allItems = items)
        }
    }

    fun loadInventory() {
        viewModelScope.launch {
            val inventory = repository.getInventoryForUser(1)
            _uiState.value = _uiState.value.copy(inventory = inventory)
        }
    }

    fun loadOutfit() {
        viewModelScope.launch {
            val outfit = repository.getOutfit(1)
            _uiState.value = _uiState.value.copy(outfit = outfit)
        }
    }

    fun loadUser() {
        viewModelScope.launch {
            val user = repository.getUser()
            _uiState.value = _uiState.value.copy(coins = user?.coins ?: 0)
        }
    }

    fun selectCategory(category: ItemCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun purchaseItem(itemId: Int) {
        viewModelScope.launch {
            val success = purchaseItemUseCase.invoke(1, itemId)
            if (success) {
                loadInventory()
                loadUser()
            }
        }
    }

    fun equipItem(inventoryId: Int) {
        viewModelScope.launch {
            val success = equipItemUseCase.invoke(1, inventoryId)
            if (success) {
                loadOutfit()
                loadInventory()
            }
        }
    }

    data class ShopUiState(
        val allItems: List<com.foxtask.app.data.local.entities.Item> = emptyList(),
        val inventory: List<Inventory> = emptyList(),
        val outfit: Outfit? = null,
        val selectedCategory: ItemCategory = ItemCategory.HAT,
        val coins: Int = 0
    ) {
        val filteredItems: List<com.foxtask.app.data.local.entities.Item>
            get() = allItems.filter { it.category == selectedCategory && it.isActive }
    }
}
