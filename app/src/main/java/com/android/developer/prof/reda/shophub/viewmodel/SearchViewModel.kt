package com.android.developer.prof.reda.shophub.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.shophub.data.Product
import com.android.developer.prof.reda.shophub.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SearchViewModel"

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _products = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val product: StateFlow<Resource<List<Product>>>
        get() = _products


    private fun fetchProducts() {
        viewModelScope.launch {
            _products.emit(Resource.Loading())
        }

        firestore.collection("products").addSnapshotListener { query, e ->
            if (e != null) {
                viewModelScope.launch {
                    _products.emit(Resource.Error(e.message.toString()))
                }
            }
            viewModelScope.launch {
                if (query != null) {
                    _products.emit(Resource.Success(query.toObjects(Product::class.java)))
                }
            }

        }
    }
}