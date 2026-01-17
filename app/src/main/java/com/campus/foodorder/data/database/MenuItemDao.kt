package com.campus.foodorder.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.campus.foodorder.data.model.MenuItem
import kotlinx.coroutines.flow.Flow

// Lab: Room Data Access Object (Phase 2)
// Purpose: Define database operations for MenuItem entity
@Dao
interface MenuItemDao {

    @Insert
    fun insert(item: MenuItem)

    @Update
    fun update(item: MenuItem)

    @Delete
    fun delete(item: MenuItem)

    @Query("SELECT * FROM menu_items")
    fun getAllMenuItems(): Flow<List<MenuItem>>

    @Query("SELECT * FROM menu_items WHERE vendorId = :vendorId")
    fun getMenuItemsByVendor(vendorId: Int): Flow<List<MenuItem>>

    @Query("DELETE FROM menu_items WHERE vendorId = :vendorId")
    fun deleteByVendor(vendorId: Int)
}