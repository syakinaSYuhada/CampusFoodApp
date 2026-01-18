package com.campus.foodorder.ui.student

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.campus.foodorder.R
import com.campus.foodorder.adapter.MenuItemAdapter
import com.campus.foodorder.data.model.MenuItem
import com.campus.foodorder.utils.NotificationHelper
import com.campus.foodorder.viewmodel.MenuViewModel
import kotlinx.coroutines.launch

// Lab: Fragment with RecyclerView, ViewModel, Notifications (Phase 2-4)
// Purpose: Student dashboard - browse menu, tap to place order
class StudentDashboardFragment : Fragment() {
    private lateinit var rvMenuItems: RecyclerView
    private lateinit var adapter: MenuItemAdapter
    private lateinit var viewModel: MenuViewModel
    private lateinit var notificationHelper: NotificationHelper

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Notification permission granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MenuViewModel::class.java)
        notificationHelper = NotificationHelper(requireContext())
        rvMenuItems = view.findViewById(R.id.rvMenuItems)

        requestNotificationPermission()

        adapter = MenuItemAdapter()
        rvMenuItems.layoutManager = LinearLayoutManager(requireContext())
        rvMenuItems.adapter = adapter

        // Phase 2 Lab Comment: Handle menu item click -> navigate to order creation
        adapter.setOnItemClickListener { menuItem ->
            val action = StudentDashboardFragmentDirections.actionToCreateOrder(menuItem)
            findNavController().navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allMenuItems.collect { items ->
                adapter.updateItems(items)
                if (items.isNotEmpty()) {
                    notificationHelper.showOrderNotification(
                        "Menu Updated",
                        "${items.size} items available"
                    )
                }
            }
        }

        insertSampleData()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
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
        viewLifecycleOwner.lifecycleScope.launch {
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
