package com.campus.foodorder.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

// Lab: Room Entities (Phase 2)
// Purpose: Data model for menu item - Parcelable for navigation args
@Parcelize
@Entity(tableName = "menu_items")
data class MenuItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val vendorId: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String = "",
    val isAvailable: Boolean = true,
    val preparationTime: Int = 20
) : Parcelable
