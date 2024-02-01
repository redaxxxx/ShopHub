package com.android.developer.prof.reda.shophub.viewmodel

import androidx.lifecycle.ViewModel
import com.android.developer.prof.reda.shophub.data.User
import com.android.developer.prof.reda.shophub.util.RegisterFailedState
import com.android.developer.prof.reda.shophub.util.RegisterValidation
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.util.validateEmail
import com.android.developer.prof.reda.shophub.util.validatePassword
import com.android.developer.prof.reda.shophub.util.validationFirstName
import com.android.developer.prof.reda.shophub.util.validationLastName
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth): ViewModel() {

    private val _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.Loading())
    val register: Flow<Resource<FirebaseUser>>
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
                        _register.value = Resource.Success(it)
                    }
                }.addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        }else{
            val registerFailedState = RegisterFailedState(
                validateEmail(user.email),
                validatePassword(password),
                validationFirstName(user.firstName),
                validationFirstName(user.lastName)
            )
            runBlocking {
                _validation.send(registerFailedState)
            }
        }

    }

    private fun checkValidation(
        user: User,
        password: String
    ): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val firstNameValidation = validationFirstName(user.firstName)
        val lastNameValidation = validationLastName(user.lastName)

        return emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success &&
                firstNameValidation is RegisterValidation.Success &&
                lastNameValidation is RegisterValidation.Success
    }
}