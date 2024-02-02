package com.android.developer.prof.reda.shophub.util

import android.util.Patterns

fun validateLoginEmail(email: String): LoginValidation{
    if (email.isEmpty()){
        return LoginValidation.Failed("Email cannot be empty")
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return LoginValidation.Failed("Wrong email format")
    }

    return LoginValidation.Success
}

fun validateLoginPassword(password: String): LoginValidation{
    if (password.isEmpty()){
        return LoginValidation.Failed("Password cannot be empty")
    }
    if (password.length < 6){
        return LoginValidation.Failed("Password should contains 6 char")
    }

    return LoginValidation.Success
}