package com.campus.foodorder.repository

import android.app.Application
import com.campus.foodorder.data.database.AppDatabase
import com.campus.foodorder.data.model.Order
import com.campus.foodorder.data.model.OrderStatus
import com.campus.foodorder.utils.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Lab: Order Business Logic (Phase 2)
class OrderRepository(private val application: Application) {
    private val orderDao = AppDatabase.getDatabase(application).orderDao()
    private val menuItemDao = AppDatabase.getDatabase(application).menuItemDao()
    private val notificationHelper = NotificationHelper(application)

    fun getStudentOrders(studentId: String) = orderDao.getStudentOrders(studentId)
    fun getVendorOrders(vendorId: Int) = orderDao.getVendorOrders(vendorId)
    fun getOrdersByStatus(status: OrderStatus) = orderDao.getOrdersByStatus(status)

    suspend fun createOrder(order: Order) = withContext(Dispatchers.IO) {
        orderDao.insert(order)
    }

    suspend fun acceptOrder(orderId: Int) = withContext(Dispatchers.IO) {
        orderDao.updateOrderStatus(orderId, OrderStatus.ACCEPTED)
        // Notify student
        notificationHelper.showOrderNotification(
            "Order Accepted",
            "Your order #$orderId has been accepted by the vendor"
        )
    }

    suspend fun rejectOrder(orderId: Int) = withContext(Dispatchers.IO) {
        orderDao.updateOrderStatus(orderId, OrderStatus.REJECTED)
        // Notify student
        notificationHelper.showOrderNotification(
            "Order Rejected",
            "Your order #$orderId was rejected by the vendor"
        )
    }

    suspend fun completeOrder(orderId: Int) = withContext(Dispatchers.IO) {
        orderDao.updateOrderStatus(orderId, OrderStatus.COMPLETED)
        // Notify student
        notificationHelper.showOrderNotification(
            "Order Completed",
            "Your order #$orderId is ready for pickup!"
        )
    }
}
