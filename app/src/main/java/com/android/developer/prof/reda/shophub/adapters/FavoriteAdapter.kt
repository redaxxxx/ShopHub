package com.android.developer.prof.reda.shophub.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.shophub.data.CartProduct
import com.android.developer.prof.reda.shophub.data.FavoriteProduct
import com.android.developer.prof.reda.shophub.data.Product
import com.android.developer.prof.reda.shophub.databinding.FavoriteItemsBinding
import com.android.developer.prof.reda.shophub.helper.getProductPrice
import com.bumptech.glide.Glide

class FavoriteAdapter: ListAdapter<FavoriteProduct, FavoriteAdapter.FavoriteViewHolder>(DiffCallback) {

    inner class FavoriteViewHolder(private val binding: FavoriteItemsBinding): ViewHolder(binding.root){
        fun bind(favoriteProduct: FavoriteProduct){
            Glide.with(itemView)
                .load(favoriteProduct.product.images[0])
                .into(binding.imageView2)
            binding.tvFavoriteProductName.text = favoriteProduct.product.name
            if(favoriteProduct.product.offerPercentage != null){
                val priceAfterOffer = favoriteProduct.product.offerPercentage.getProductPrice(
                    favoriteProduct.product.price, favoriteProduct.product.offerPercentage)
                binding.newPriceFavoriteProduct.text = "$ ${String.format("%.2f", priceAfterOffer)}"

                binding.oldPriceFavoriteProduct.paintFlags = binding.oldPriceFavoriteProduct.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }else {
                binding.newPriceFavoriteProduct.visibility = View.INVISIBLE
            }

            binding.oldPriceFavoriteProduct.text = "$ ${favoriteProduct.product.price}"

            binding.deleteFavoriteProductBtn.setOnClickListener {
                onDelete?.invoke(favoriteProduct)
            }

            binding.detailsButton.setOnClickListener {
                onDetailsProduct?.invoke(favoriteProduct.product)
            }

        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<FavoriteProduct>(){
        override fun areItemsTheSame(oldItem: FavoriteProduct, newItem: FavoriteProduct): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: FavoriteProduct,
            newItem: FavoriteProduct
        ): Boolean {
            return oldItem.product.id == newItem.product.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(FavoriteItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteProduct = getItem(position)
        holder.bind(favoriteProduct)
    }

    var onDelete: ((FavoriteProduct)-> Unit) ?= null
    var onDetailsProduct: ((Product)-> Unit) ?= null
}