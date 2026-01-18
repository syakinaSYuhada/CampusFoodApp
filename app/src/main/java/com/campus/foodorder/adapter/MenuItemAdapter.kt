package com.campus.foodorder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.campus.foodorder.R
import com.campus.foodorder.data.model.MenuItem

// Optimized RecyclerView adapter using ListAdapter + DiffUtil
class MenuItemAdapter(
    private var onItemClick: ((MenuItem) -> Unit)? = null
) : ListAdapter<MenuItem, MenuItemAdapter.ViewHolder>(DIFF_CALLBACK) {

    fun setOnItemClickListener(listener: (MenuItem) -> Unit) {
        onItemClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvItemName)
        private val tvDesc: TextView = itemView.findViewById(R.id.tvItemDesc)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvItemPrice)

        fun bind(item: MenuItem) {
            tvName.text = item.name
            tvDesc.text = item.description
            tvPrice.text = "RM ${String.format("%.2f", item.price)}"

            itemView.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MenuItem>() {
            override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
                // Assuming unique ID is provided by Room entity (e.g., primary key)
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
