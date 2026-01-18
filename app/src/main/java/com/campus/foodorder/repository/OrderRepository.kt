package com.campus.foodorder.repository

import android.app.Application
import com.campus.foodorder.data.database.AppDatabase
import com.campus.foodorder.data.model.Order
import com.campus.foodorder.data.model.OrderStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Lab: Order Business Logic (Phase 2)
class OrderRepository(application: Application) {
    private val orderDao = AppDatabase.getDatabase(application).orderDao()
    private val menuItemDao = AppDatabase.getDatabase(application).menuItemDao()

    fun getStudentOrders(studentId: String) = orderDao.getStudentOrders(studentId)
    fun getVendorOrders(vendorId: Int) = orderDao.getVendorOrders(vendorId)
    fun getOrdersByStatus(status: OrderStatus) = orderDao.getOrdersByStatus(status)

    suspend fun createOrder(order: Order) = withContext(Dispatchers.IO) {
        orderDao.insert(order)
    }

    suspend fun acceptOrder(orderId: Int) = withContext(Dispatchers.IO) {
        orderDao.updateOrderStatus(orderId, OrderStatus.ACCEPTED)
    }

    suspend fun rejectOrder(orderId: Int) = withContext(Dispatchers.IO) {
        orderDao.updateOrderStatus(orderId, OrderStatus.REJECTED)
    }

    suspend fun completeOrder(orderId: Int) = withContext(Dispatchers.IO) {
        val order = orderDao.getOrder(orderId)
        // In a real app, would use collect or similar reactive approach
        orderDao.updateOrderStatus(orderId, OrderStatus.COMPLETED)
    }
}
