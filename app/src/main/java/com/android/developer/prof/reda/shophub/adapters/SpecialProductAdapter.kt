package com.android.developer.prof.reda.shophub.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.shophub.data.Product
import com.android.developer.prof.reda.shophub.databinding.SpecialRvItemBinding
import com.android.developer.prof.reda.shophub.helper.getProductPrice
import com.bumptech.glide.Glide

class SpecialProductAdapter(val onClickListener: OnItemClickListener) : ListAdapter<Product, SpecialProductAdapter.SpecialProductViewHolder>(DiffCallback) {

    inner class SpecialProductViewHolder(private val binding: SpecialRvItemBinding): ViewHolder(binding.root){
        fun bind(product: Product){
            binding.apply {
                Glide.with(itemView)
                    .load(product.images[0])
                    .into(binding.imgSpecialRvItem)

                tvNameSpecialRvItem.text = product.name
                if(product.offerPercentage != null){
                    val priceAfterOffer = product.offerPercentage.getProductPrice(product.price, product.offerPercentage)
                    tvNewPriceSpecialRvItem.text = "$ ${String.format("%.2f", priceAfterOffer)}"

                    tvPriceSpecialRvItem.paintFlags = tvPriceSpecialRvItem.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }else {
                    tvNewPriceSpecialRvItem.visibility = View.INVISIBLE
                }

                tvPriceSpecialRvItem.text = "$ ${product.price}"
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductViewHolder {
        return SpecialProductViewHolder(SpecialRvItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: SpecialProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.itemView.setOnClickListener{
            onClickListener.onClick(product)
        }
        holder.bind(product)
    }

    class OnItemClickListener(val clickListener: (product: Product) -> Unit) {
        fun onClick(product: Product) = clickListener(product)
    }
}