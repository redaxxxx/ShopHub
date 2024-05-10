package com.android.developer.prof.reda.shophub.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.shophub.data.order.Order
import com.android.developer.prof.reda.shophub.databinding.OrderItemsBinding
import java.text.SimpleDateFormat

class OrderAdapter : ListAdapter<Order, OrderAdapter.OrderViewHolder>(DiffCallback) {

    inner class OrderViewHolder(private val binding: OrderItemsBinding): ViewHolder(binding.root){
        fun bind(order: Order){
            binding.tvOrderID.text = order.id.substring(0, 8)

            binding.tvOrderDate.text = order.date
            binding.tvOrderStatus.text = order.orderStatus
            binding.tvOrderItems.text = "${order.products.size} items purchased"
            binding.tvOrderPrice.text = order.totalPrice.toString()

            itemView.setOnClickListener{
                onOrderClick?.invoke(order)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(OrderItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order)
    }

    var onOrderClick: ((Order) -> Unit) ?= null
}