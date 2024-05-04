package com.android.developer.prof.reda.shophub.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteProduct (
    val product: Product,
    val isFavorite: Boolean

):Parcelable{
    constructor(): this(Product(), false)
}