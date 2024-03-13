package com.android.developer.prof.reda.shophub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.shophub.data.CartProduct
import com.android.developer.prof.reda.shophub.firebase.FirebaseCommon
import com.android.developer.prof.reda.shophub.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel(){

    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProducts: StateFlow<Resource<List<CartProduct>>>
        get() = _cartProducts

    private var cartProductDocument = emptyList<DocumentSnapshot>()
    init {
        fetchCartProducts()
    }
    private fun fetchCartProducts(){
        viewModelScope.launch {
            _cartProducts.emit(Resource.Loading())
        }

        firestore.collection("user").document(firebaseAuth.uid!!).collection("cart")
            .addSnapshotListener{value, error ->
                if (error != null && value == null){
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Error(error.message.toString()))
                    }
                }else {
                    viewModelScope.launch {
                        if (value != null) {
                            cartProductDocument = value.documents
                        }
                        _cartProducts.emit(Resource.Success(value!!.toObjects(CartProduct::class.java)))
                    }
                }
            }
    }

    fun quantityChanging(
        cartProduct: CartProduct,
        quantityChanging: FirebaseCommon.QuantityChanging
    ){
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if (index != null && index != -1){
            val documentId = cartProductDocument[index].id
            when(quantityChanging){
                FirebaseCommon.QuantityChanging.INCREASE ->{
                    increaseQuantity(documentId)
                }
                FirebaseCommon.QuantityChanging.DECREASE ->{
                    decreaseQuantity(documentId)
                }
            }
        }
    }

    private fun increaseQuantity(documentId: String) {
        firebaseCommon.increaseQuantityProduct(documentId){result, exception->
            if (exception != null){
                viewModelScope.launch {
                    _cartProducts.emit(Resource.Error(exception.message.toString()))
                }
            }
        }
    }

    private fun decreaseQuantity(documentId: String) {
        firebaseCommon.decreaseQuantityProduct(documentId){result, exception->
            if (exception != null){
                viewModelScope.launch {
                    _cartProducts.emit(Resource.Error(exception.message.toString()))
                }
            }
        }
    }

}