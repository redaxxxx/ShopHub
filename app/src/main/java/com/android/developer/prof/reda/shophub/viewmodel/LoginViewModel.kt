package com.android.developer.prof.reda.shophub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.shophub.data.User
import com.android.developer.prof.reda.shophub.util.LoginFailedState
import com.android.developer.prof.reda.shophub.util.LoginValidation
import com.android.developer.prof.reda.shophub.util.RegisterFailedState
import com.android.developer.prof.reda.shophub.util.RegisterValidation
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.util.validateLoginEmail
import com.android.developer.prof.reda.shophub.util.validateLoginPassword
import com.android.developer.prof.reda.shophub.util.validateRegisterEmail
import com.android.developer.prof.reda.shophub.util.validateRegisterPassword
import com.android.developer.prof.reda.shophub.util.validationFirstName
import com.android.developer.prof.reda.shophub.util.validationLastName
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
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