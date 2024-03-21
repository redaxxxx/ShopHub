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

class CartProductsAdapter(val context: Context): ListAdapter<CartProduct, CartProductsAdapter.CartProductsViewHolder>(DiffCallback) {

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

            if(cartProduct.product.offerPercentage != null){
                val priceAfterOffer = cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price, cartProduct.product.offerPercentage)
                binding.productPriceInCart.text = "$ ${String.format("%.2f", priceAfterOffer)}"
            }else{
                binding.productPriceInCart.text = "$ ${cartProduct.product.price}"
            }


            binding.textQuantity.text = cartProduct.quantity.toString()

            binding.selectedColorProduct.removeAllViews()
            val chip = Chip(context)
            chip.chipStrokeColor = ColorStateList.valueOf(Color.BLACK)
            chip.chipStrokeWidth = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                3F,
                context.resources.displayMetrics
            )

            when(cartProduct.selectedColor) {
                "black" -> chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#FF000000"))
                "white" ->chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#FFFFFFFF"))
                "red" ->  chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#FF0000"))
                "green" ->chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#00FF00"))
                "blue" -> chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#0000FF"))
                "yellow" -> chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#FFFF00"))
                "cyan" -> chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#00FFFF"))
                "magenta" -> chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#FF00FF"))
                "gray" ->chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#808080"))
                "coffee" ->chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#6F4E37"))
                else -> Unit
            }

            chip.isCheckable = true

            binding.selectedColorProduct.addView(chip)

            binding.selectedColorProduct.invalidate()

            binding.plusButton.setOnClickListener {
                onPlusClick?.invoke(cartProduct)
            }

            binding.minusButton.setOnClickListener {
                onMinusClick?.invoke(cartProduct)
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

    var onPlusClick: ((CartProduct) -> Unit) ?= null
    var onMinusClick: ((CartProduct) -> Unit) ?= null
}