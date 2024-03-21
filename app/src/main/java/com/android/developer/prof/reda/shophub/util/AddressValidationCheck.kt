package com.android.developer.prof.reda.shophub.util

fun validationAddFirstName(firstName: String): AddressValidation{
    if (firstName.isEmpty()){
        return AddressValidation.Failed("FirstName cannot be empty")
    }
    return AddressValidation.Success
}
fun validationFamilyName(familyName: String): AddressValidation{
    if (familyName.isEmpty()){
        return AddressValidation.Failed("Family name cannot be empty")
    }
    return AddressValidation.Success
}
fun validationPhoneNumber(phoneNumber: String): AddressValidation{
    if (phoneNumber.isEmpty()){
        return AddressValidation.Failed("Phone Number cannot be empty")
    }
    return AddressValidation.Success
}
fun validationAnotherPhoneNumber(anotherPhoneNumber: String): AddressValidation{
    if (anotherPhoneNumber.isEmpty()){
        return AddressValidation.Failed("Another Phone Number cannot be empty")
    }
    return AddressValidation.Success
}
fun validationAddress(address: String): AddressValidation{
    if (address.isEmpty()){
        return AddressValidation.Failed("Address cannot be empty")
    }
    return AddressValidation.Success
}
fun validationState(state: String): AddressValidation{
    if (state.isEmpty()){
        return AddressValidation.Failed("state cannot be empty")
    }
    return AddressValidation.Success
}
fun validationCity(city: String): AddressValidation{
    if (city.isEmpty()){
        return AddressValidation.Failed("city cannot be empty")
    }
    return AddressValidation.Success
}