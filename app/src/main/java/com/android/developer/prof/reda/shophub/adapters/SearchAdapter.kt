package com.android.developer.prof.reda.shophub.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.shophub.data.Product
import com.android.developer.prof.reda.shophub.databinding.TextRowLayoutBinding

class SearchAdapter: ListAdapter<Product, SearchAdapter.SearchViewHolder>(DiffCallback) {

    inner class SearchViewHolder(private val binding: TextRowLayoutBinding): ViewHolder(binding.root){
        fun bind(product: Product){
            binding.productName.text = product.name
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(TextRowLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }
}