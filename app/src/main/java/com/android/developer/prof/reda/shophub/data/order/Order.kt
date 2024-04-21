package com.android.developer.prof.reda.shophub.data.order

import com.android.developer.prof.reda.shophub.data.Address
import com.android.developer.prof.reda.shophub.data.CartProduct

data class Order(
    val orderStatus: String,
    val totalPrice: Float,
    val products: List<CartProduct>,
    val address: Address
)