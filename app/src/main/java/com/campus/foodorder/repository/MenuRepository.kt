package com.campus.foodorder.repository

import android.app.Application
import com.campus.foodorder.data.database.AppDatabase
import com.campus.foodorder.data.model.MenuItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Lab: Repository Pattern (Phase 2)
// Purpose: Abstract data access logic from ViewModel
// Repository wraps database calls with withContext(Dispatchers.IO) for clean threading
class MenuRepository(application: Application) {
    private val menuItemDao = AppDatabase.getDatabase(application).menuItemDao()

    fun getAllMenuItems() = menuItemDao.getAllMenuItems()

    fun getMenuItemsByVendor(vendorId: Int) = menuItemDao.getMenuItemsByVendor(vendorId)

    suspend fun getCount(): Int = withContext(Dispatchers.IO) { menuItemDao.getCount() }

    suspend fun insertMenuItem(item: MenuItem) = withContext(Dispatchers.IO) {
        menuItemDao.insert(item)
    }

    suspend fun updateMenuItem(item: MenuItem) = withContext(Dispatchers.IO) {
        menuItemDao.update(item)
    }

    suspend fun deleteMenuItem(item: MenuItem) = withContext(Dispatchers.IO) {
        menuItemDao.delete(item)
    }

    suspend fun deleteByVendor(vendorId: Int) = withContext(Dispatchers.IO) {
        menuItemDao.deleteByVendor(vendorId)
    }
}
