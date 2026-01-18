package com.campus.foodorder.ui.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import com.campus.foodorder.R
import com.campus.foodorder.data.model.Order
import com.campus.foodorder.databinding.FragmentCreateOrderBinding
import com.campus.foodorder.viewmodel.OrderViewModel

/**
 * CreateOrderFragment - Phase 2: Order Creation Interface
 * 
 * Students can:
 * - View menu item details
 * - Select quantity with +/- buttons
 * - Add special notes/instructions
 * - See real-time total price calculation
 * - Place order (stored in local SQLite database)
 *
 * Database Communication: Order saved to orders table
 * Vendor queries: SELECT * FROM orders WHERE vendorId = ? AND status = 'PENDING'
 */
class CreateOrderFragment : Fragment() {

    private var _binding: FragmentCreateOrderBinding? = null
    private val binding get() = _binding!!

    private val args: CreateOrderFragmentArgs by navArgs()
    private val orderViewModel: OrderViewModel by viewModels()

    private var quantity = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        val menuItem = args.menuItem
        binding.apply {
            itemNameText.text = menuItem.name
            categoryText.text = "Category: ${menuItem.category}"
            descriptionText.text = menuItem.description
            priceText.text = "RM %.2f".format(menuItem.price)
            prepTimeText.text = "${menuItem.preparationTime} min"
            updateTotalPrice()
        }
    }

    private fun setupListeners() {
        binding.apply {
            minusButton.setOnClickListener {
                if (quantity > 1) {
                    quantity--
                    quantityText.text = quantity.toString()
                    updateTotalPrice()
                }
            }

            plusButton.setOnClickListener {
                quantity++
                quantityText.text = quantity.toString()
                updateTotalPrice()
            }

            placeOrderButton.setOnClickListener {
                placeOrder()
            }

            cancelButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun updateTotalPrice() {
        val menuItem = args.menuItem
        val total = menuItem.price * quantity
        binding.totalPriceText.text = "RM %.2f".format(total)
    }

    private fun placeOrder() {
        val menuItem = args.menuItem
        val studentId = requireActivity().intent.getStringExtra("student_id") ?: "student@utem.edu.my"

        if (quantity < 1) {
            showError("Please select a quantity")
            return
        }

        val order = Order(
            menuItemId = menuItem.id,
            studentId = studentId,
            vendorId = menuItem.vendorId,
            quantity = quantity,
            totalPrice = menuItem.price * quantity,
            status = com.campus.foodorder.data.model.OrderStatus.PENDING,
            notes = binding.notesInput.text.toString(),
            createdAt = System.currentTimeMillis(),
            completedAt = null
        )

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                orderViewModel.createOrder(order)
                Snackbar.make(binding.root, "Order placed! Vendor will respond soon.", Snackbar.LENGTH_LONG).show()
                kotlinx.coroutines.delay(1500)
                findNavController().popBackStack()
            } catch (e: Exception) {
                showError("Failed: ${e.message}")
            }
        }
    }

    private fun showError(message: String) {
        binding.statusMessage.apply {
            text = message
            visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
