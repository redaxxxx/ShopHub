package com.android.developer.prof.reda.shophub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.shophub.data.Address
import com.android.developer.prof.reda.shophub.util.AddNewAddressFailedState
import com.android.developer.prof.reda.shophub.util.AddressValidation
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.util.validationAddFirstName
import com.android.developer.prof.reda.shophub.util.validationAddress
import com.android.developer.prof.reda.shophub.util.validationAnotherPhoneNumber
import com.android.developer.prof.reda.shophub.util.validationCity
import com.android.developer.prof.reda.shophub.util.validationFamilyName
import com.android.developer.prof.reda.shophub.util.validationFirstName
import com.android.developer.prof.reda.shophub.util.validationPhoneNumber
import com.android.developer.prof.reda.shophub.util.validationState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
@HiltViewModel
class AddressViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
)
:ViewModel() {

    private val _addNewAddress = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val addNewAddress: StateFlow<Resource<Address>>
        get() = _addNewAddress

    private val _validation = Channel<AddNewAddressFailedState>()
    val validation = _validation.receiveAsFlow()

    fun addNewAddress(address: Address){
        if (isValidation(address)){
            viewModelScope.launch {
                _addNewAddress.emit(Resource.Loading())
            }
            firestore.collection("user").document(auth.uid!!).collection("address")
                .document().set(address)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        _addNewAddress.emit(Resource.Success(address))
                    }
                }.addOnFailureListener {
                    viewModelScope.launch{
                        _addNewAddress.emit(Resource.Error(it.message.toString()))
                    }
                }
        }else {
            val addNewAddressFailedState = AddNewAddressFailedState(
            validationAddFirstName(address.firstName),
            validationFamilyName(address.familyName),
            validationPhoneNumber(address.phoneNumber),
            validationAddress(address.address),
            validationState(address.state),
                validationCity(address.city)
            )

            runBlocking {
                _validation.send(addNewAddressFailedState)
            }
        }

    }


    fun isValidation(newAddress: Address):Boolean{
        val firstName = validationAddFirstName(newAddress.firstName)
        val familyName = validationFamilyName(newAddress.familyName)
        val phoneNumber = validationPhoneNumber(newAddress.phoneNumber)
        val address = validationAddress(newAddress.address)
        val state = validationState(newAddress.state)
        val city = validationCity(newAddress.city)

        return firstName is AddressValidation.Success &&
                familyName is AddressValidation.Success &&
                phoneNumber is AddressValidation.Success &&
                address is AddressValidation.Success &&
                state is AddressValidation.Success &&
                city is AddressValidation.Success

    }

}