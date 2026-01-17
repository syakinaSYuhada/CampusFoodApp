package com.campus.foodorder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.campus.foodorder.utils.NotificationHelper

// Lab: BroadcastReceiver (Phase 3)
// Purpose: Handle background notification events for order updates
class OrderNotificationReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_ORDER_READY = "com.campus.foodorder.ORDER_READY"
        const val EXTRA_ITEM_NAME = "item_name"
        const val EXTRA_ESTIMATED_TIME = "estimated_time"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notificationHelper = NotificationHelper(context)
        
        when (intent.action) {
            ACTION_ORDER_READY -> {
                val itemName = intent.getStringExtra(EXTRA_ITEM_NAME) ?: "Your order"
                notificationHelper.showOrderReadyNotification(itemName)
            }
            Intent.ACTION_BOOT_COMPLETED -> {
                // Re-schedule any pending notifications after device reboot
            }
        }
    }
}
