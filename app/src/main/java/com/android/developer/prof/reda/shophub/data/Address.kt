package com.android.developer.prof.reda.shophub.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    val firstName: String,
    val familyName: String,
    val phoneNumber: String,
    val anotherPhoneNumber: String,
    val address: String,
    val moreAddressDetails: String,
    val state: String,
    val city: String
): Parcelable
{
    constructor(): this("", "", "", "", "", "", "","")
}