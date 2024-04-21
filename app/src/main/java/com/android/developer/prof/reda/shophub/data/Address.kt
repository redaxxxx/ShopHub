package com.android.developer.prof.reda.shophub.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    val id: String,
    val firstName: String,
    val familyName: String,
    val country: String,
    val phoneNumber: String,
    val anotherPhoneNumber: String? =null,
    val address: String,
    val moreAddressDetails: String? = null,
    val state: String,
    val city: String,
    val postalCode: Int
): Parcelable
{
    constructor(): this("","", "", "","", "", "", "", "","", 0)
}