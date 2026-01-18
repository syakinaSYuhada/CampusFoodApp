package com.campus.foodorder.ui.student

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.campus.foodorder.R
import com.campus.foodorder.adapter.MenuItemAdapter
import com.campus.foodorder.data.model.MenuItem
import com.campus.foodorder.utils.NotificationHelper
import com.campus.foodorder.viewmodel.MenuViewModel
import kotlinx.coroutines.launch

// Lab: Activities, RecyclerView, ViewModel, Notifications (Phase 2 & 3)
// Purpose: Student dashboard to browse food items with notification support
class StudentDashboardActivity : AppCompatActivity() {
    private lateinit var rvMenuItems: RecyclerView
    private lateinit var adapter: MenuItemAdapter
    private lateinit var viewModel: MenuViewModel
    private lateinit var notificationHelper: NotificationHelper

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dashboard)

        viewModel = ViewModelProvider(this).get(MenuViewModel::class.java)
        notificationHelper = NotificationHelper(this)
        rvMenuItems = findViewById(R.id.rvMenuItems)

        // Request notification permission for Android 13+
        requestNotificationPermission()

        adapter = MenuItemAdapter()
        rvMenuItems.layoutManager = LinearLayoutManager(this)
        rvMenuItems.setHasFixedSize(true)
        rvMenuItems.adapter = adapter

        // Observe menu items from ViewModel
        var lastCount = -1
        lifecycleScope.launch {
            viewModel.allMenuItems.collect { items ->
                adapter.submitList(items)
                // Debounce notifications on count change only
                val count = items.size
                if (count != lastCount) {
                    lastCount = count
                    if (count > 0) {
                        notificationHelper.showOrderNotification(
                            "Menu Updated",
                            "${count} items available"
                        )
                    }
                }
            }
        }

        // Guarded seeding: only insert sample data when DB is empty
        lifecycleScope.launch {
            val count = viewModel.getMenuCount()
            if (count == 0) {
                insertSampleData()
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission already granted
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun insertSampleData() {
        lifecycleScope.launch {
            val sampleItems = listOf(
                MenuItem(
                    vendorId = 1,
                    name = "Nasi Lemak",
                    description = "Aromatic coconut rice with sambal",
                    price = 4.50,
                    category = "Rice",
                    preparationTime = 15
                ),
                MenuItem(
                    vendorId = 1,
                    name = "Roti Canai",
                    description = "Crispy Indian flat bread",
                    price = 2.00,
                    category = "Bread",
                    preparationTime = 10
                ),
                MenuItem(
                    vendorId = 2,
                    name = "Char Kuey Teow",
                    description = "Stir-fried noodles with seafood",
                    price = 6.00,
                    category = "Noodles",
                    preparationTime = 12
                )
            )
            sampleItems.forEach { viewModel.insertMenuItem(it) }
        }
    }
}
