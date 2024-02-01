package com.android.developer.prof.reda.shophub.util

sealed class RegisterValidation(){
    object Success: RegisterValidation()
    data class Failed(val message: String): RegisterValidation()
}
data class RegisterFailedState(
    val email: RegisterValidation,
    val password: RegisterValidation,
    val firstName: RegisterValidation,
    val lastName: RegisterValidation
)
