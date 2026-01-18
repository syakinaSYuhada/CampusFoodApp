package com.campus.foodorder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.campus.foodorder.data.model.Order
import com.campus.foodorder.data.model.OrderStatus
import com.campus.foodorder.repository.OrderRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Lab: Order ViewModel (Phase 2)
class OrderViewModel(application: Application) : AndroidViewModel(application) {
    private val orderRepository = OrderRepository(application)

    fun getStudentOrders(studentId: String): StateFlow<List<Order>> =
        orderRepository.getStudentOrders(studentId)
            .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Lazily, emptyList())

    fun getVendorOrders(vendorId: Int): StateFlow<List<Order>> =
        orderRepository.getVendorOrders(vendorId)
            .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Lazily, emptyList())

    fun createOrder(order: Order) = viewModelScope.launch {
        orderRepository.createOrder(order)
    }

    fun acceptOrder(orderId: Int) = viewModelScope.launch {
        orderRepository.acceptOrder(orderId)
    }

    fun rejectOrder(orderId: Int) = viewModelScope.launch {
        orderRepository.rejectOrder(orderId)
    }

    fun completeOrder(orderId: Int) = viewModelScope.launch {
        orderRepository.completeOrder(orderId)
    }
}
