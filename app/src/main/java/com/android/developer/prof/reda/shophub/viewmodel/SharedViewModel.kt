package com.android.developer.prof.reda.shophub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.shophub.data.Address
import com.android.developer.prof.reda.shophub.data.CartProduct
import com.android.developer.prof.reda.shophub.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedViewModel: ViewModel() {
    private val _productOrder = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())

    private val _addressOrder = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val addressOrder: StateFlow<Resource<Address>>
        get() = _addressOrder

    fun setAddressInfo(address: Address){
        viewModelScope.launch {
            _addressOrder.emit(Resource.Success(address))
        }
    }
}