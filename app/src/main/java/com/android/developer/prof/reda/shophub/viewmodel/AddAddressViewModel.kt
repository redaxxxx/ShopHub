package com.android.developer.prof.reda.shophub.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.shophub.data.Address
import com.android.developer.prof.reda.shophub.util.AddNewAddressFailedState
import com.android.developer.prof.reda.shophub.util.AddressValidation
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.util.validationAddFirstName
import com.android.developer.prof.reda.shophub.util.validationAddress
import com.android.developer.prof.reda.shophub.util.validationCity
import com.android.developer.prof.reda.shophub.util.validationFamilyName
import com.android.developer.prof.reda.shophub.util.validationPhoneNumber
import com.android.developer.prof.reda.shophub.util.validationState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _addNewAddress = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val addNewAddress: StateFlow<Resource<Address>>
        get() = _addNewAddress

    private val _updatedAddress = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val updatedAddress: StateFlow<Resource<Address>>
        get() = _updatedAddress

    private val _validation = Channel<AddNewAddressFailedState>()
    val validation = _validation.receiveAsFlow()

    fun addNewAddress(address: Address) {
        if (isValidation(address)) {
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
                    viewModelScope.launch {
                        _addNewAddress.emit(Resource.Error(it.message.toString()))
                    }
                }
        } else {
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

    fun updateAddress(address: Address)  =  CoroutineScope(Dispatchers.IO).launch{
        val map = HashMap<String, Any>()
        map["firstName"] = address.firstName
        map["familyName"] = address.familyName
        map["country"] = address.country
        map["phoneNumber"] = address.phoneNumber
        map["anotherPhoneNumber"] = address.anotherPhoneNumber.orEmpty()
        map["address"] = address.address
        map["moreAddressDetails"] = address.moreAddressDetails.orEmpty()
        map["state"] = address.state
        map["city"] = address.city
        map["postalCode"] = address.postalCode

        firestore.collection("user").document(auth.uid!!).collection("address")
            .whereEqualTo("id", address.id).get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()){
                    for (document in it) {
                        Log.d("AddressViewModel", "DocumentID: ${document.id}")
                        firestore.collection("user").document(auth.uid!!)
                            .collection("address").document(document.id)
                            .update(map)
                            .addOnSuccessListener {
                                viewModelScope.launch {
                                    _updatedAddress.emit(Resource.Success(address))
                                }
                            }.addOnFailureListener {
                                viewModelScope.launch {
                                    _updatedAddress.emit(Resource.Error(it.message.toString()))
                                }
                            }
                    }
                }else {
                    Log.d("AddressViewModel", "Address Query is Empty")
                }
            }
            .addOnFailureListener {
                Log.d("AddressViewModel", it.message.toString())
            }.await()



    }

    private fun isValidation(newAddress: Address): Boolean {
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