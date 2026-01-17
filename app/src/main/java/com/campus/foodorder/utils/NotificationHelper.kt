package com.campus.foodorder.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.campus.foodorder.R
import com.campus.foodorder.ui.student.StudentDashboardActivity

// Lab: Notifications (Phase 3)
// Purpose: Centralized notification management with proper channel setup
class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "campus_food_orders"
        const val CHANNEL_NAME = "Food Orders"
        const val CHANNEL_DESC = "Notifications for food order updates"
        const val NOTIFICATION_ID = 1001
    }

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC
                enableVibration(true)
                enableLights(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showOrderNotification(title: String, message: String) {
        val intent = Intent(context, StudentDashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun showOrderReadyNotification(itemName: String) {
        showOrderNotification(
            "Order Ready!",
            "Your $itemName is ready for pickup"
        )
    }

    fun showOrderConfirmedNotification(itemName: String, estimatedTime: Int) {
        showOrderNotification(
            "Order Confirmed",
            "$itemName will be ready in $estimatedTime minutes"
        )
    }
}
