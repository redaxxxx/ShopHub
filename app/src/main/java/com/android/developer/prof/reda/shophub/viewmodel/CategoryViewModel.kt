package com.android.developer.prof.reda.shophub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.shophub.data.Category
import com.android.developer.prof.reda.shophub.data.Product
import com.android.developer.prof.reda.shophub.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val firestore: FirebaseFirestore,
    private val category: Category
): ViewModel() {

    private val _offerProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val offerProduct: StateFlow<Resource<List<Product>>>
        get() = _offerProducts

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProduct: StateFlow<Resource<List<Product>>>
        get() = _bestProducts

    init {
        fetchBestProducts()
        fetchOfferProducts()
    }
    fun fetchOfferProducts(){
        viewModelScope.launch {
            _offerProducts.emit(Resource.Loading())
        }

        firestore.collection("products").whereEqualTo("category", category.category)
            .whereNotEqualTo("offerPercentage", null).get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _offerProducts.emit(Resource.Success(products))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _offerProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestProducts(){
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
        }

        firestore.collection("products").whereEqualTo("category", category.category)
            .whereEqualTo("offerPercentage", null).get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(products))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }


}