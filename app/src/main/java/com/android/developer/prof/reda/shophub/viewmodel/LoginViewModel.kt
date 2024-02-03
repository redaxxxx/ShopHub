package com.android.developer.prof.reda.shophub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.shophub.util.LoginFailedState
import com.android.developer.prof.reda.shophub.util.LoginValidation
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.util.validateLoginEmail
import com.android.developer.prof.reda.shophub.util.validateLoginPassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login: SharedFlow<Resource<FirebaseUser>>
        get() = _login

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword: SharedFlow<Resource<String>>
        get() = _resetPassword

    private val _validation = Channel<LoginFailedState>()
    val validation = _validation.receiveAsFlow()

    fun login(email: String, password: String){
        if (checkValidation(email, password)) {
            runBlocking {
                _login.emit(Resource.Loading())
            }
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        it.user?.let {
                            _login.emit(Resource.Success(it))
                        }
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _login.emit(Resource.Error(it.message.toString()))
                    }
                }
        }else{
            val loginFailedState = LoginFailedState(
                validateLoginEmail(email),
                validateLoginPassword(password)
            )
            runBlocking {
                _validation.send(loginFailedState)
            }
        }
    }

    fun resetPassword(email: String){
        runBlocking {
            _resetPassword.emit(Resource.Loading())
        }
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _resetPassword.emit(Resource.Success(email))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _resetPassword.emit(Resource.Error(it.message.toString()))
                }
            }

    }
    private fun checkValidation(
        email: String,
        password: String
    ): Boolean {
        val emailValidation = validateLoginEmail(email)
        val passwordValidation = validateLoginPassword(password)

        return emailValidation is LoginValidation.Success &&
                passwordValidation is LoginValidation.Success
    }
}