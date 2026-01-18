package com.campus.foodorder.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.campus.foodorder.data.model.Order
import com.campus.foodorder.data.model.OrderStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert
    fun insert(order: Order)

    @Update
    fun update(order: Order)

    @Query("SELECT * FROM orders WHERE studentId = :studentId ORDER BY createdAt DESC")
    fun getStudentOrders(studentId: String): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE vendorId = :vendorId ORDER BY createdAt DESC")
    fun getVendorOrders(vendorId: Int): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE id = :orderId")
    fun getOrder(orderId: Int): Flow<Order?>

    @Query("SELECT * FROM orders WHERE status = :status ORDER BY createdAt DESC")
    fun getOrdersByStatus(status: OrderStatus): Flow<List<Order>>

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    fun updateOrderStatus(orderId: Int, status: OrderStatus)
}
