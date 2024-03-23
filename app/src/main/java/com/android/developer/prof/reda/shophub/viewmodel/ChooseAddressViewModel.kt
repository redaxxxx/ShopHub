package com.android.developer.prof.reda.shophub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.shophub.data.Address
import com.android.developer.prof.reda.shophub.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ChooseAddressViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel(){
    private val _addresses = MutableStateFlow<Resource<List<Address>>>(Resource.Unspecified())
    val addresses: StateFlow<Resource<List<Address>>>
        get() = _addresses

    private var addressDocument = emptyList<DocumentSnapshot>()

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
                    viewModelScope.launch { _addresses.emit(Resource.Error(error.message.toString())) }
                }
                else{
                    viewModelScope.launch {

                        _addresses.emit(Resource.Success(value!!.toObjects(Address::class.java)))
                    }
                }
            }
    }
}