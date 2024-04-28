package com.android.developer.prof.reda.shophub.data.order

import android.os.Parcelable
import com.android.developer.prof.reda.shophub.data.Address
import com.android.developer.prof.reda.shophub.data.CartProduct
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    val id: String,
    val orderStatus: String,
    val totalPrice: Float,
    val products: List<CartProduct>,
    val address: Address?,
    val date: String
): Parcelable
{
    constructor(): this("", "", 0f, emptyList(), null, "")
}