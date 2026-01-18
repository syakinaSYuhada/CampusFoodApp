package com.campus.foodorder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.campus.foodorder.data.model.MenuItem
import com.campus.foodorder.repository.MenuRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// Lab: Menu ViewModel (Phase 2)
class MenuViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MenuRepository(application)

    val allMenuItems: Flow<List<MenuItem>> = repository.getAllMenuItems()

    suspend fun getMenuCount(): Int = repository.getCount()

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
