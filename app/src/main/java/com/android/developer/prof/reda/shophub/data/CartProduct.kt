package com.android.developer.prof.reda.shophub.data

data class CartProduct(
    val product: Product,
    val quantity: Int,
    val selectedColor: String? = null,
    val selectedSize: String? = null
){
    constructor() : this(Product(), 1, null, null)
}
