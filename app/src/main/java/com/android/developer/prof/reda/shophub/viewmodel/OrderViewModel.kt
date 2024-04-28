package com.android.developer.prof.reda.shophub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.shophub.data.order.Order
import com.android.developer.prof.reda.shophub.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
):ViewModel() {

    private val _order = MutableStateFlow<Resource<Order>>(Resource.Unspecified())
    val order: StateFlow<Resource<Order>>
        get() = _order

    private val _orders = MutableStateFlow<Resource<List<Order>>>(Resource.Unspecified())
    val orders: StateFlow<Resource<List<Order>>>
        get() = _orders

    init {
        fetchOrders()
    }
    private fun fetchOrders(){
        viewModelScope.launch {
            _orders.emit(Resource.Loading())
        }

        firestore.collection("user").document(auth.uid!!).collection("orders")
            .get()
            .addOnSuccessListener {
                viewModelScope.launch {
                    _orders.emit(Resource.Success(it.toObjects(Order::class.java)))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _order.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun placedOrder(order: Order){
        viewModelScope.launch {
            _order.emit(Resource.Loading())
        }

        firestore.runBatch{
            //TODO: Add the order into user-orders collection
            //TODO: Add the order into orders collection
            //TODO: Delete the products from user-cart collection

            firestore.collection("user").document(auth.uid!!).collection("orders")
                .document().set(order)

            firestore.collection("orders").document().set(order)

            firestore.collection("user").document(auth.uid!!).collection("cart")
                .get()
                .addOnSuccessListener {
                    it.documents.forEach {
                        it.reference.delete()
                    }
                }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _order.emit(Resource.Success(order))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _order.emit(Resource.Error(it.message.toString()))
            }
        }
    }
}