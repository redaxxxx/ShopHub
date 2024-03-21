package com.android.developer.prof.reda.shophub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.shophub.data.CartProduct
import com.android.developer.prof.reda.shophub.firebase.FirebaseCommon
import com.android.developer.prof.reda.shophub.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
): ViewModel() {

    private val _addToProduct = MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    val addToProduct: StateFlow<Resource<CartProduct>>
        get() = _addToProduct

    fun addUpdateProductInCart(cartProduct: CartProduct){
        viewModelScope.launch {
            _addToProduct.emit(Resource.Loading())
        }

        firestore.collection("user").document(auth.uid!!).collection("cart")
            .whereEqualTo("products.id", cartProduct.product.id).get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()){
                        //add new product
                        addNewProduct(cartProduct)
                    }else{
                        val product = it.first().toObject(CartProduct::class.java)
                        if (product == cartProduct){
                            //increase quantity
                            val documentId = it.first().id
                            increaseQuantity(documentId, cartProduct)

                        }else{
                            //add new Product
                            addNewProduct(cartProduct)
                        }
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _addToProduct.emit(Resource.Error(it.message.toString()))
                }
            }
    }
    private fun addNewProduct(cartProduct: CartProduct){
        firebaseCommon.addNewProduct(cartProduct){addedProduct, exception ->
            viewModelScope.launch {
               if (exception == null){
                   _addToProduct.emit(Resource.Success(addedProduct!!))
               }else{
                   _addToProduct.emit(Resource.Error(exception.message.toString()))
               }
            }

        }
    }

    private fun increaseQuantity(documentId: String, cartProduct: CartProduct){
        firebaseCommon.increaseQuantityProduct(documentId){_, e ->
            viewModelScope.launch {
                if (e == null){
                    _addToProduct.emit(Resource.Success(cartProduct))
                }else{
                    _addToProduct.emit(Resource.Error(e.message.toString()))
                }
            }
        }
    }
}