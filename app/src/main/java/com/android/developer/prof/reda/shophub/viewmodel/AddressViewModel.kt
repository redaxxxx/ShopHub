package com.android.developer.prof.reda.shophub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.shophub.data.Address
import com.android.developer.prof.reda.shophub.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel(){
    private val _addresses = MutableStateFlow<Resource<List<Address>>>(Resource.Unspecified())
    val addresses: StateFlow<Resource<List<Address>>>
        get() = _addresses

    init {
        fetchAddresses()
    }
    private fun fetchAddresses(){
        viewModelScope.launch {
            _addresses.emit(Resource.Loading())
        }

        firestore.collection("user").document(auth.uid!!).collection("address")
            .addSnapshotListener{value, error->
                if (error != null && value == null){
                    viewModelScope.launch {
                        _addresses.emit(Resource.Error(error.message.toString()))
                    }
                }
                else{
                    viewModelScope.launch {
                        _addresses.emit(Resource.Success(value!!.toObjects(Address::class.java)))
                    }
                }
            }
    }

    fun deleteAddress(address: Address){
        firestore.collection("user").document(auth.uid!!).collection("address")
            .whereEqualTo("id", address.id).get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()){
                    for (document in it){
                        firestore.collection("user").document(auth.uid!!).collection("address")
                            .document(document.id).delete()
                    }
                }
            }
    }

}