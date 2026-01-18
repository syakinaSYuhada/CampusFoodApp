package com.campus.foodorder.ui.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.campus.foodorder.R
import com.campus.foodorder.adapter.OrderAdapter
import com.campus.foodorder.viewmodel.OrderViewModel
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

// Student Orders Fragment - View order history with status tracking
class StudentOrdersFragment : Fragment() {
    private lateinit var rvOrders: RecyclerView
    private lateinit var tvEmptyState: TextView
    private lateinit var adapter: OrderAdapter
    private lateinit var viewModel: OrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)

        // Setup toolbar navigation
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Setup RecyclerView
        rvOrders = view.findViewById(R.id.rvOrders)
        tvEmptyState = view.findViewById(R.id.tvEmptyState)
        
        adapter = OrderAdapter()
        rvOrders.layoutManager = LinearLayoutManager(requireContext())
        rvOrders.setHasFixedSize(true)
        rvOrders.adapter = adapter

        // Observe student orders (hardcoded student ID for now)
        // In production, would get from auth/shared preferences
        val studentId = "student@campus.edu"
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getStudentOrders(studentId).collect { orders ->
                adapter.submitList(orders)
                
                // Toggle empty state
                if (orders.isEmpty()) {
                    tvEmptyState.visibility = View.VISIBLE
                    rvOrders.visibility = View.GONE
                } else {
                    tvEmptyState.visibility = View.GONE
                    rvOrders.visibility = View.VISIBLE
                }
            }
        }
    }
}
