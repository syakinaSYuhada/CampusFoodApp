package com.campus.foodorder.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.campus.foodorder.R
import com.campus.foodorder.data.model.Order
import com.campus.foodorder.data.model.OrderStatus
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import java.text.SimpleDateFormat
import java.util.*

// Vendor order adapter with Accept/Reject actions for pending orders
class VendorOrderAdapter(
    private val onAcceptClick: (Order) -> Unit,
    private val onRejectClick: (Order) -> Unit
) : ListAdapter<Order, VendorOrderAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vendor_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvOrderItemName: TextView = itemView.findViewById(R.id.tvOrderItemName)
        private val tvStudentInfo: TextView = itemView.findViewById(R.id.tvStudentInfo)
        private val tvOrderQuantity: TextView = itemView.findViewById(R.id.tvOrderQuantity)
        private val tvOrderPrice: TextView = itemView.findViewById(R.id.tvOrderPrice)
        private val tvOrderNotes: TextView = itemView.findViewById(R.id.tvOrderNotes)
        private val tvOrderTimestamp: TextView = itemView.findViewById(R.id.tvOrderTimestamp)
        private val chipStatus: Chip = itemView.findViewById(R.id.chipStatus)
        private val layoutActions: LinearLayout = itemView.findViewById(R.id.layoutActions)
        private val btnAccept: MaterialButton = itemView.findViewById(R.id.btnAccept)
        private val btnReject: MaterialButton = itemView.findViewById(R.id.btnReject)

        fun bind(order: Order) {
            // In real app, would join with MenuItem to get name
            tvOrderItemName.text = "Order #${order.menuItemId}"
            tvStudentInfo.text = "Student: ${order.studentId}"
            tvOrderQuantity.text = "Qty: ${order.quantity}"
            tvOrderPrice.text = "RM ${String.format("%.2f", order.totalPrice)}"

            // Show notes if present
            if (order.notes.isNotEmpty()) {
                tvOrderNotes.visibility = View.VISIBLE
                tvOrderNotes.text = order.notes
            } else {
                tvOrderNotes.visibility = View.GONE
            }

            // Format timestamp
            val dateFormat = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
            tvOrderTimestamp.text = dateFormat.format(Date(order.createdAt))

            // Style status chip and show/hide actions
            chipStatus.text = order.status.name
            when (order.status) {
                OrderStatus.PENDING -> {
                    chipStatus.setChipBackgroundColorResource(android.R.color.holo_orange_light)
                    chipStatus.setTextColor(Color.WHITE)
                    layoutActions.visibility = View.VISIBLE
                }
                OrderStatus.ACCEPTED -> {
                    chipStatus.setChipBackgroundColorResource(android.R.color.holo_blue_light)
                    chipStatus.setTextColor(Color.WHITE)
                    layoutActions.visibility = View.GONE
                }
                OrderStatus.REJECTED -> {
                    chipStatus.setChipBackgroundColorResource(android.R.color.holo_red_light)
                    chipStatus.setTextColor(Color.WHITE)
                    layoutActions.visibility = View.GONE
                }
                OrderStatus.COMPLETED -> {
                    chipStatus.setChipBackgroundColorResource(android.R.color.holo_green_light)
                    chipStatus.setTextColor(Color.WHITE)
                    layoutActions.visibility = View.GONE
                }
            }

            // Set action button listeners
            btnAccept.setOnClickListener {
                onAcceptClick(order)
            }
            btnReject.setOnClickListener {
                onRejectClick(order)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem == newItem
            }
        }
    }
}
