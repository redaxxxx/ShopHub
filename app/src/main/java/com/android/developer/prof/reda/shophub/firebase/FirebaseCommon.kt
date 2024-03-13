package com.android.developer.prof.reda.shophub.firebase

import com.android.developer.prof.reda.shophub.data.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseCommon(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){

    private val cartCollection = firestore.collection("user").document(auth.uid!!)
        .collection("cart")

    fun addNewProduct(cartProduct: CartProduct, onResult: (CartProduct?, Exception?) -> Unit){
        cartCollection.document().set(cartProduct)
            .addOnSuccessListener {
                onResult(cartProduct, null)
            }.addOnFailureListener {
                onResult(null, it)
            }
    }

    fun increaseQuantityProduct(documentId: String, onResult: (String?, Exception?) -> Unit){
        firestore.runTransaction {transition->
            val documentRef = firestore.document(documentId)
            val document = transition.get(documentRef)
            val productObject = document.toObject(CartProduct:: class.java)
            productObject?.let {cartProduct ->
                val newQuantity = cartProduct.quantity +1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef, newProductObject)
            }
        }.addOnSuccessListener {
            onResult(documentId, null)
        }.addOnFailureListener {
            onResult(null, it)
        }
    }

    fun decreaseQuantityProduct(documentId: String, onResult: (String?, Exception?) -> Unit){
        firestore.runTransaction {transition->
            val documentRef = firestore.document(documentId)
            val document = transition.get(documentRef)
            val productObject = document.toObject(CartProduct:: class.java)
            productObject?.let {cartProduct ->
                val newQuantity = cartProduct.quantity - 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef, newProductObject)
            }
        }.addOnSuccessListener {
            onResult(documentId, null)
        }.addOnFailureListener {
            onResult(null, it)
        }
    }

    enum class QuantityChanging{
        INCREASE, DECREASE
    }
}