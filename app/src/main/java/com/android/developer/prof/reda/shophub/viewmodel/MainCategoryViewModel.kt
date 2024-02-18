package com.android.developer.prof.reda.shophub.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.shophub.data.Product
import com.android.developer.prof.reda.shophub.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainCategoryViewModel"
@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())

    val specialProducts: StateFlow<Resource<List<Product>>>
        get() = _specialProducts

    private val _bestDeals = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())

    val bestDeals: StateFlow<Resource<List<Product>>>
        get() = _bestDeals

    private val _bestProduct = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())

    val bestProduct: StateFlow<Resource<List<Product>>>
        get() = _bestProduct

    init {
        fetchSpecialProducts()
        fetchBestDeals()
        fetchBestProduct()
    }

    private fun fetchBestProduct() {
        viewModelScope.launch {
            _bestProduct.emit(Resource.Loading())
        }

        firestore.collection("products")
            .whereEqualTo("category", "Best Product").get()
            .addOnSuccessListener {result->
                val bestProductList = result.toObjects(Product::class.java)
                viewModelScope.launch{
                    _bestProduct.emit(Resource.Success(bestProductList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestProduct.emit(Resource.Error(it.message.toString()))
                    Log.d(TAG, it.message.toString())
                }

            }
    }

    private fun fetchBestDeals() {
        viewModelScope.launch {
            _bestDeals.emit(Resource.Loading())
        }

        firestore.collection("products")
            .whereEqualTo("category", "Best Deals").get()
            .addOnSuccessListener {result->
                val bestDealsList = result.toObjects(Product::class.java)
                viewModelScope.launch{
                    _bestDeals.emit(Resource.Success(bestDealsList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Error(it.message.toString()))
                    Log.d(TAG, it.message.toString())
                }

            }
    }

    private fun fetchSpecialProducts(){
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())
        }
        firestore.collection("products")
            .whereEqualTo("category", "Special Products").get()
            .addOnSuccessListener {result->
                val specialProductsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Success(specialProductsList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Error(it.message.toString()))
                    Log.d(TAG, it.message.toString())
                }
            }

    }
}