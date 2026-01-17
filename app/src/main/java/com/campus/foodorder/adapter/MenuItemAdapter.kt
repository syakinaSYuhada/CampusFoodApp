package com.campus.foodorder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.campus.foodorder.R
import com.campus.foodorder.data.model.MenuItem

// Lab: RecyclerView & ViewHolder (Phase 2)
// Purpose: Display menu items list with proper binding
class MenuItemAdapter(private var items: List<MenuItem> = emptyList()) :
    RecyclerView.Adapter<MenuItemAdapter.ViewHolder>() {

    fun updateItems(newItems: List<MenuItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvItemName)
        private val tvDesc: TextView = itemView.findViewById(R.id.tvItemDesc)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvItemPrice)

        fun bind(item: MenuItem) {
            tvName.text = item.name
            tvDesc.text = item.description
            tvPrice.text = "RM ${String.format("%.2f", item.price)}"
        }
    }
}