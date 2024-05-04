package com.android.developer.prof.reda.shophub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.shophub.data.CartProduct
import com.android.developer.prof.reda.shophub.data.FavoriteProduct
import com.android.developer.prof.reda.shophub.data.Product
import com.android.developer.prof.reda.shophub.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel(){

    private val _favoriteProducts = MutableStateFlow<Resource<List<FavoriteProduct>>>(Resource.Unspecified())
    val favoriteProduct: StateFlow<Resource<List<FavoriteProduct>>>
        get() = _favoriteProducts

    init {
        fetchFavoriteProducts()
    }
    private fun fetchFavoriteProducts(){
        firestore.collection("user").document(auth.uid!!).collection("favorites")
            .get()
            .addOnSuccessListener {
                viewModelScope.launch {
                    _favoriteProducts.emit(Resource.Success(it.toObjects(FavoriteProduct::class.java)))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _favoriteProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun deleteFavoriteProduct(favoriteProduct: FavoriteProduct){
        firestore.collection("user").document(auth.uid!!).collection("favorites")
            .whereEqualTo("id", favoriteProduct.product.id).get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()){
                    it.documents.forEach {
                        firestore.collection("user").document(auth.uid!!).collection("favorites")
                            .document(it.id).delete()
                    }
                }
            }
    }

}