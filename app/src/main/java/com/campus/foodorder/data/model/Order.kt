package com.campus.foodorder.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Lab: Order Entity (Phase 2 Business Logic)
@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val menuItemId: Int,           // FK to MenuItem
    val studentId: String,          // Student email/ID
    val vendorId: Int,              // Vendor ID who owns menu item
    val quantity: Int,
    val totalPrice: Double,
    val status: OrderStatus = OrderStatus.PENDING,  // PENDING, ACCEPTED, REJECTED, COMPLETED
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)

enum class OrderStatus {
    PENDING,    // Student just ordered
    ACCEPTED,   // Vendor accepted
    REJECTED,   // Vendor rejected
    COMPLETED   // Order delivered
}
