package com.android.developer.prof.reda.shophub.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.shophub.data.Product
import com.android.developer.prof.reda.shophub.databinding.BestDealsRvItemBinding
import com.android.developer.prof.reda.shophub.helper.getProductPrice
import com.bumptech.glide.Glide

class BestDealsAdapter(val onClickListener: OnItemClickListener): ListAdapter<Product, BestDealsAdapter.BestDealsViewHolder>(DiffCallback) {

    inner class BestDealsViewHolder(private val binding: BestDealsRvItemBinding): ViewHolder(binding.root){
        fun bind(product: Product){

            binding.apply {
                Glide.with(itemView)
                    .load(product.images[0])
                    .into(imgBestDeal)

                tvDealProductName.text = product.name

                if(product.offerPercentage != null){
                    val priceAfterOffer = product.offerPercentage.getProductPrice(product.price, product.offerPercentage)
                    tvNewPrice.text = "$ ${String.format("%.2f", priceAfterOffer)}"

                    tvOldPrice.paintFlags = tvOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }else {
                    tvNewPrice.visibility = View.INVISIBLE
                }

                tvOldPrice.text = "$ ${product.price}"
            }
        }
    }

    object DiffCallback: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDealsViewHolder {
        return BestDealsViewHolder(BestDealsRvItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: BestDealsViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)

        holder.itemView.setOnClickListener{
            onClickListener.onClick(product)
        }
    }

    class OnItemClickListener(val clickListener: (product: Product) -> Unit) {
        fun onClick(product: Product) = clickListener(product)
    }
}