package com.android.developer.prof.reda.shophub.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.shophub.data.CartProduct
import com.android.developer.prof.reda.shophub.databinding.CartProductItemBinding
import com.android.developer.prof.reda.shophub.helper.getProductPrice
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class CartProductsAdapter(): ListAdapter<CartProduct, CartProductsAdapter.CartProductsViewHolder>(DiffCallback) {

    companion object DiffCallback: DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

    }
    inner class CartProductsViewHolder(private val binding: CartProductItemBinding): ViewHolder(binding.root){
        fun bind(cartProduct: CartProduct){
            Glide.with(itemView.context)
                .load(cartProduct.product.images[0])
                .into(binding.imgProductCart)

            binding.textProductInCart.text = cartProduct.product.name

            val priceAfterOffer = cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)
            binding.productPriceInCart.text = "$ ${String.format("%.2f", priceAfterOffer)}"

            binding.textQuantity.text = cartProduct.quantity.toString()

            when(cartProduct.selectedColor) {
                "black" -> binding.productColorsInCart.setBackgroundColor(Color.parseColor("#000000"))

                "white" -> binding.productColorsInCart.setBackgroundColor(Color.parseColor("#FFFFFF"))
                "red" ->  binding.productColorsInCart.setBackgroundColor(Color.parseColor("#FF0000"))
                "green" -> binding.productColorsInCart.setBackgroundColor(Color.parseColor("#00FF00"))
                "blue" -> binding.productColorsInCart.setBackgroundColor(Color.parseColor("0000FF"))
                "yellow" -> binding.productColorsInCart.setBackgroundColor(Color.parseColor("#FFFF00"))
                "cyan" -> binding.productColorsInCart.setBackgroundColor(Color.parseColor("#00FFFF"))
                "magenta" -> binding.productColorsInCart.setBackgroundColor(Color.parseColor("#FF00FF"))
                "gray" -> binding.productColorsInCart.setBackgroundColor(Color.parseColor("#808080"))
                "coffee" ->binding.productColorsInCart.setBackgroundColor(Color.parseColor("#6F4E37"))
                else -> Unit
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductsViewHolder {
        return CartProductsViewHolder(CartProductItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: CartProductsViewHolder, position: Int) {
        val cartProduct = getItem(position)
        holder.bind(cartProduct)
    }
    private fun getColorMap(colorSelected: String): Map<String, String>{
        val colorMap = mutableMapOf<String, String>()

        when(colorSelected){
            "black" -> colorMap["black"] = "#000000"
            "white" ->colorMap["white"] = "#FFFFFF"
            "red" ->colorMap["red"] = "#FF0000"
            "green" ->colorMap["green"] = "#00FF00"
            "blue" -> colorMap["blue"] = "0000FF"
            "yellow" -> colorMap["yellow"] = "#FFFF00"
            "cyan" -> colorMap["cyan"] = "#00FFFF"
            "magenta" -> colorMap["magenta"] = "#FF00FF"
            "gray" -> colorMap["gray"] = "#808080"
            "coffee" -> colorMap["coffee"] = "#6F4E37"
            else -> Unit
        }
        return colorMap
    }
}