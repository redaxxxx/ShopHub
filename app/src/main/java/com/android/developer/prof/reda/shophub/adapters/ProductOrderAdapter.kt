package com.android.developer.prof.reda.shophub.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.shophub.data.CartProduct
import com.android.developer.prof.reda.shophub.data.order.Order
import com.android.developer.prof.reda.shophub.databinding.ProductOrderItemsBinding
import com.android.developer.prof.reda.shophub.helper.getProductPrice
import com.bumptech.glide.Glide

class ProductOrderAdapter : ListAdapter<CartProduct, ProductOrderAdapter.ProductOrderViewHolder>(DiffCallback) {

    inner class ProductOrderViewHolder(private val binding: ProductOrderItemsBinding): ViewHolder(binding.root){
        fun bind(cartProduct: CartProduct){
            Glide.with(itemView)
                .load(cartProduct.product.images[0])
                .into(binding.tvProductOrderImg)

            binding.tvProductOrderName.text = cartProduct.product.name

            if(cartProduct.product.offerPercentage != null){
                val priceAfterOffer = cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price, cartProduct.product.offerPercentage)
                binding.tvProductOrderPrice.text = "$ ${String.format("%.2f", priceAfterOffer)}"
            }else{
                binding.tvProductOrderPrice.text = "$ ${cartProduct.product.price}"
            }
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductOrderViewHolder {
        return ProductOrderViewHolder(ProductOrderItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ProductOrderViewHolder, position: Int) {
        val orderProduct = getItem(position)
        holder.bind(orderProduct)
    }
}