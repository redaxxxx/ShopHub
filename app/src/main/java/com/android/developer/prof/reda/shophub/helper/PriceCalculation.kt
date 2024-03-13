package com.android.developer.prof.reda.shophub.helper

fun Float?.getProductPrice(price: Float): Float {
    if (this == null) {
        return price
    }
    val remainingPricePercentage = 1f - this

    return remainingPricePercentage * price
}