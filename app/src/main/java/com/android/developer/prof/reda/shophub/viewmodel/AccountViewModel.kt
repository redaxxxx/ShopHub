package com.android.developer.prof.reda.shophub.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.graphics.get
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.shophub.ShopHubApplication
import com.android.developer.prof.reda.shophub.data.User
import com.android.developer.prof.reda.shophub.util.Resource
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

private const val TAG = "AccountViewModel"
@HiltViewModel
class AccountViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage : StorageReference,
    private val app: Application
): AndroidViewModel(app) {

    private val _userInfo = MutableStateFlow<Resource<User?>>(Resource.Unspecified())
    val userInfo: StateFlow<Resource<User?>>
        get() = _userInfo

    private val _updateUserInfo = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val updateUserInfo: StateFlow<Resource<User>>
        get() = _updateUserInfo
    init {
        fetchUserInfo()
    }
    private fun fetchUserInfo(){
        viewModelScope.launch {
            _userInfo.emit(Resource.Loading())
        }

        firestore.collection("user").document(auth.uid!!).get()
            .addOnSuccessListener {
                viewModelScope.launch {
                    _userInfo.emit(Resource.Success(it.toObject(User::class.java)))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _userInfo.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun updateUserImg(user: User, imgUri: Uri){
        val areInputsValid = user.firstName.trim().isNotEmpty() &&
                user.lastName.trim().isNotEmpty()

        if (!areInputsValid){
            viewModelScope.launch {
                _userInfo.emit(Resource.Error("Check your Inputs"))
            }
        }
        viewModelScope.launch {
            _userInfo.emit(Resource.Loading())
        }

        if (imgUri == null){
            saveUserInformation(user, true)
        }else{
            saveUserInformationWithNewImage(user, imgUri)
        }
    }

    private fun saveUserInformationWithNewImage(user: User, imgUri: Uri) {
        viewModelScope.launch {
            try{
                val imgBitmap = MediaStore.Images.Media.getBitmap(
                    getApplication<ShopHubApplication>().contentResolver,
                    imgUri
                )

                val byteArrayOutputStream = ByteArrayOutputStream()
                imgBitmap.compress(Bitmap.CompressFormat.JPEG, 96, byteArrayOutputStream)
                val imgByteArray = byteArrayOutputStream.toByteArray()
                val imgDirectory = storage.child("profileImages/${auth.uid}/${UUID.randomUUID()}")
                val result = imgDirectory.putBytes(imgByteArray).await()
                val imgUrl = result.storage.downloadUrl.await().toString()
                saveUserInformation(user.copy(imagePath = imgUrl), false)
            }catch (e: Exception){
                Log.d(TAG, e.message.toString())
            }
        }
    }

    private fun saveUserInformation(user: User, shouldRetrievedOldImage: Boolean) {
        firestore.runTransaction {
            val documentRef = firestore.collection("user").document(auth.uid!!)
            if (shouldRetrievedOldImage){
                val currentUser = it.get(documentRef).toObject(User::class.java)
                val newUser = user.copy(imagePath = currentUser?.imagePath ?: "")
                it.set(documentRef, newUser)
            }else{
                it.set(documentRef, user)
            }
        }.addOnSuccessListener {
            viewModelScope.launch{
                _updateUserInfo.emit(Resource.Success(user))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _updateUserInfo.emit(Resource.Error(it.message.toString()))
            }
        }
    }
}