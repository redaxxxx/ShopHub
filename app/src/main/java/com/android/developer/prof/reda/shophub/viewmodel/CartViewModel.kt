package com.android.developer.prof.reda.shophub.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.shophub.data.CartProduct
import com.android.developer.prof.reda.shophub.firebase.FirebaseCommon
import com.android.developer.prof.reda.shophub.helper.getProductPrice
import com.android.developer.prof.reda.shophub.util.ADD_ADDRESS_FRAGMENT
import com.android.developer.prof.reda.shophub.util.BILLING_FRAGMENT
import com.android.developer.prof.reda.shophub.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

private const val TAG = "CartViewModel"
@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel(){

    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProducts: StateFlow<Resource<List<CartProduct>>>
        get() = _cartProducts

    private val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deleteDialog: SharedFlow<CartProduct>
        get() = _deleteDialog

    private var cartProductDocument = emptyList<DocumentSnapshot>()

    private val _navigate = MutableStateFlow(0)
    val navigate: StateFlow<Int>
        get() = _navigate
    fun deleteItemFromCart(cartProduct: CartProduct){
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if (index != null && index != -1){
            val documentId = cartProductDocument[index].id

            firestore.collection("user").document(firebaseAuth.uid!!).collection("cart")
                .document(documentId).delete()
        }
    }

    val productsPrice = cartProducts.map {
        when(it){
            is Resource.Success-> {
                calculateTotalPrice(it.data!!)
            }
            else-> null
        }
    }

    private fun calculateTotalPrice(data: List<CartProduct>): Float {
        return data.sumByDouble { cartProduct ->
            (cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price, cartProduct.product.offerPercentage)
                    * cartProduct.quantity).toDouble()
        }.toFloat()
    }
    init {
        fetchCartProducts()

        firestore.collection("user").document(firebaseAuth.uid!!).collection("address").get()
            .addOnSuccessListener {
                if (it.isEmpty){
                    viewModelScope.launch{
                        _navigate.emit(ADD_ADDRESS_FRAGMENT)
                    }
                }else{
                    viewModelScope.launch {
                        _navigate.emit(BILLING_FRAGMENT)
                    }
                }
            }
            .addOnFailureListener {
                Log.d(TAG, it.message.toString())
            }
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
                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
                    increaseQuantity(documentId)
                }
                FirebaseCommon.QuantityChanging.DECREASE ->{
                    if (cartProduct.quantity == 1){
                        viewModelScope.launch { _deleteDialog.emit(cartProduct) }
                        return
                    }
                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
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