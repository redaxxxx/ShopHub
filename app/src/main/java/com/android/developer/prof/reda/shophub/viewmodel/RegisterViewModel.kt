package com.android.developer.prof.reda.shophub.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.developer.prof.reda.shophub.data.User
import com.android.developer.prof.reda.shophub.util.RegisterFailedState
import com.android.developer.prof.reda.shophub.util.RegisterValidation
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.util.USER_COLLECTION
import com.android.developer.prof.reda.shophub.util.validateRegisterEmail
import com.android.developer.prof.reda.shophub.util.validateRegisterPassword
import com.android.developer.prof.reda.shophub.util.validationFirstName
import com.android.developer.prof.reda.shophub.util.validationLastName
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private const val TAG = "RegisterViewModel"
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore):
    ViewModel() {

    private val _register = MutableStateFlow<Resource<User>>(Resource.Loading())
    val register: Flow<Resource<User>>
        get() = _register

    private val _validation = Channel<RegisterFailedState>()
    val validation = _validation.receiveAsFlow()
    fun createAccountWithEmailAndPassword(user: User, password: String){

        if (checkValidation(user, password)) {
            runBlocking {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener { result ->
                    result.user?.let {
                        saveUserInfo(it.uid, user)
                    }
                }.addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        }else{
            val registerFailedState = RegisterFailedState(
                validateRegisterEmail(user.email),
                validateRegisterPassword(password),
                validationFirstName(user.firstName),
                validationFirstName(user.lastName)
            )
            runBlocking {
                _validation.send(registerFailedState)
            }
        }

    }

    private fun saveUserInfo(userUid: String, user: User) {
        db.collection(USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }.addOnFailureListener {
                Log.d(TAG, it.message.toString())
                _register.value = Resource.Error(it.message.toString())
            }
    }

    private fun checkValidation(
        user: User,
        password: String
    ): Boolean {
        val emailValidation = validateRegisterEmail(user.email)
        val passwordValidation = validateRegisterPassword(password)
        val firstNameValidation = validationFirstName(user.firstName)
        val lastNameValidation = validationLastName(user.lastName)

        return emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success &&
                firstNameValidation is RegisterValidation.Success &&
                lastNameValidation is RegisterValidation.Success
    }
}