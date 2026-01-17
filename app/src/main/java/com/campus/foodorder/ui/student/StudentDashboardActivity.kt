package com.campus.foodorder.ui.student

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.campus.foodorder.R
import com.campus.foodorder.adapter.MenuItemAdapter
import com.campus.foodorder.data.model.MenuItem
import com.campus.foodorder.viewmodel.MenuViewModel
import kotlinx.coroutines.launch

// Lab: Activities, RecyclerView, ViewModel (Phase 2)
// Purpose: Student dashboard to browse and order food items
class StudentDashboardActivity : AppCompatActivity() {
    private lateinit var rvMenuItems: RecyclerView
    private lateinit var adapter: MenuItemAdapter
    private lateinit var viewModel: MenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dashboard)

        viewModel = ViewModelProvider(this).get(MenuViewModel::class.java)
        rvMenuItems = findViewById(R.id.rvMenuItems)

        // Setup RecyclerView
        adapter = MenuItemAdapter()
        rvMenuItems.layoutManager = LinearLayoutManager(this)
        rvMenuItems.adapter = adapter

        // Observe menu items from ViewModel
        lifecycleScope.launch {
            viewModel.allMenuItems.collect { items ->
                adapter.updateItems(items)
            }
        }

        // Insert sample data if database is empty (for demo)
        insertSampleData()
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
            sampleItems.forEach { item ->
                viewModel.insertMenuItem(item)
            }
        }
    }
}