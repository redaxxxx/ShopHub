package com.android.developer.prof.reda.shophub.util

import com.android.developer.prof.reda.shophub.data.Address
import com.android.developer.prof.reda.shophub.viewmodel.AddressViewModel

sealed class AddressValidation {
    data object Success: AddressValidation()
    data class Failed(val message : String): AddressValidation()
}
data class AddNewAddressFailedState(
    val firstName: AddressValidation,
    val familyName: AddressValidation,
    val phoneNumber: AddressValidation,
    val anotherPhoneNumber: AddressValidation,
    val address: AddressValidation,
    val state: AddressValidation,
    val city: AddressValidation
)