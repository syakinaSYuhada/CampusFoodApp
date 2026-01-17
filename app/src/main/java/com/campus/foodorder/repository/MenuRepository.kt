package com.campus.foodorder.repository

import android.content.Context
import com.campus.foodorder.data.database.AppDatabase
import com.campus.foodorder.data.model.MenuItem
import kotlinx.coroutines.flow.Flow

// Lab: Repository Pattern (Phase 2)
// Purpose: Centralize data access logic and abstract database operations
class MenuRepository(context: Context) {
    private val menuItemDao = AppDatabase.getDatabase(context).menuItemDao()

    fun getAllMenuItems(): Flow<List<MenuItem>> = menuItemDao.getAllMenuItems()

    fun getMenuItemsByVendor(vendorId: Int): Flow<List<MenuItem>> = 
        menuItemDao.getMenuItemsByVendor(vendorId)

    suspend fun insertMenuItem(item: MenuItem) = menuItemDao.insert(item)

    suspend fun updateMenuItem(item: MenuItem) = menuItemDao.update(item)

    suspend fun deleteMenuItem(item: MenuItem) = menuItemDao.delete(item)

    suspend fun deleteByVendor(vendorId: Int) = menuItemDao.deleteByVendor(vendorId)
}