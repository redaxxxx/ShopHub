package com.android.developer.prof.reda.shophub.helper

fun Float?.getProductPrice(price: Float, offer: Float): Float {

    val remainingPricePercentage = 1f - (offer.div(100))

    return remainingPricePercentage * price
}