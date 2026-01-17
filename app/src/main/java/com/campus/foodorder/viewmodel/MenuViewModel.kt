package com.campus.foodorder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.campus.foodorder.data.model.MenuItem
import com.campus.foodorder.repository.MenuRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Lab: ViewModel & StateFlow (Phase 2)
// Purpose: Manage menu data lifecycle and handle business logic
class MenuViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MenuRepository(application)

    // Expose menu items as StateFlow for reactive updates
    val allMenuItems: StateFlow<List<MenuItem>> = repository.getAllMenuItems()
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Lazily, emptyList())

    fun getVendorMenuItems(vendorId: Int): StateFlow<List<MenuItem>> =
        repository.getMenuItemsByVendor(vendorId)
            .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Lazily, emptyList())

    fun insertMenuItem(item: MenuItem) = viewModelScope.launch {
        repository.insertMenuItem(item)
    }

    fun updateMenuItem(item: MenuItem) = viewModelScope.launch {
        repository.updateMenuItem(item)
    }

    fun deleteMenuItem(item: MenuItem) = viewModelScope.launch {
        repository.deleteMenuItem(item)
    }
}