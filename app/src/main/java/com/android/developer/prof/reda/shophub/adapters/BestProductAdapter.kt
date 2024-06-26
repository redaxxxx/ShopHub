package com.android.developer.prof.reda.shophub.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.shophub.data.Product
import com.android.developer.prof.reda.shophub.databinding.ProductItemBinding
import com.android.developer.prof.reda.shophub.helper.getProductPrice
import com.bumptech.glide.Glide

class BestProductAdapter(val onclickListener: OnItemCLickListener): ListAdapter<Product, BestProductAdapter.BestProductViewHolder>(DiffCallback) {
    inner class BestProductViewHolder(private val binding: ProductItemBinding): ViewHolder(binding.root) {
        fun bind(product: Product){
            binding.apply {
                Glide.with(itemView)
                    .load(product.images[0])
                    .into(imgProduct)

                tvProductName.text = product.name
                if(product.offerPercentage != null){
                    val priceAfterOffer = product.offerPercentage.getProductPrice(product.price, product.offerPercentage)
                    tvNewPrice.text = "$ ${String.format("%.2f", priceAfterOffer)}"

                    tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    val percentage = (priceAfterOffer / product.price) - 1
                    tvPercentage.text = "${percentage}%"
                }else {
                    tvNewPrice.visibility = View.INVISIBLE
                }

                tvPrice.text = "$ ${product.price}"

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductViewHolder {
        return BestProductViewHolder(ProductItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: BestProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.itemView.setOnClickListener{
            onclickListener.onClick(product)
        }
        holder.bind(product)
    }

    class OnItemCLickListener(val clickListener: (product: Product) -> Unit){
        fun onClick(product: Product) = clickListener(product)
    }

}