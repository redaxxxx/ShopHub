package com.android.developer.prof.reda.shophub.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.shophub.data.CartProduct
import com.android.developer.prof.reda.shophub.databinding.BillingProductItemBinding
import com.android.developer.prof.reda.shophub.helper.getProductPrice
import com.bumptech.glide.Glide

class BillingProductAdapter: ListAdapter<CartProduct, BillingProductAdapter.BillingProductViewHolder>(DiffCallback) {

    inner class BillingProductViewHolder(private val binding: BillingProductItemBinding) : ViewHolder(binding.root){
        fun bind(cartProduct: CartProduct){
            Glide.with(itemView)
                .load(cartProduct.product.images[0])
                .into(binding.imgBillingRvItem)

            binding.billingProductName.text = cartProduct.product.name
            binding.tvBillingProductColor.text = cartProduct.product.colors!![0]

            if(cartProduct.product.offerPercentage != null){
                val priceAfterOffer = cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price, cartProduct.product.offerPercentage)
                binding.billingProductPrice.text = "$ ${String.format("%.2f", priceAfterOffer)}"
            }else{
                binding.billingProductPrice.text = "$ ${cartProduct.product.price}"
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product == newItem.product
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingProductViewHolder {
        return BillingProductViewHolder(
            BillingProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
    }

    override fun onBindViewHolder(holder: BillingProductViewHolder, position: Int) {
        val cartProduct = getItem(position)
        holder.bind(cartProduct)
    }
}