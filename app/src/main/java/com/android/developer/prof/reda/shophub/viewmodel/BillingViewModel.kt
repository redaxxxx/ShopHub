package com.android.developer.prof.reda.shophub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.shophub.data.Address
import com.android.developer.prof.reda.shophub.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel(){

    private val _latestAddress = MutableStateFlow<Resource<List<Address?>>>(Resource.Unspecified())
    val latestAddress : StateFlow<Resource<List<Address?>>>
        get() = _latestAddress

    private val _address = MutableStateFlow<Resource<Address?>>(Resource.Unspecified())
    val address : StateFlow<Resource<Address?>>
        get() = _address

    init {
        getLatestAddress()
    }
    private fun getLatestAddress(){
        firestore.collection("user").document(auth.uid!!).collection("address")
            .get()
            .addOnSuccessListener {
                viewModelScope.launch {
                    _latestAddress.emit(Resource.Success(it.toObjects(Address::class.java)))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _latestAddress.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun getAddress(){
        firestore.collection("user").document(auth.uid!!).collection("address")
            .document().get()
            .addOnSuccessListener {
                if (it != null){
                    viewModelScope.launch {
                        _address.emit(Resource.Success(it.toObject(Address::class.java)))
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _address.emit(Resource.Error(it.message.toString()))
                }
            }
    }

}