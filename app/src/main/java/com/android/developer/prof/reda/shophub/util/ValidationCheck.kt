package com.android.developer.prof.reda.shophub.util

import android.util.Patterns

fun validateEmail(email: String): RegisterValidation{
    if (email.isEmpty()){
        return RegisterValidation.Failed("Email cannot be empty")
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return RegisterValidation.Failed("Wrong email format")
    }

    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation{
    if (password.isEmpty()){
        return RegisterValidation.Failed("Password cannot be empty")
    }
    if (password.length < 6){
        return RegisterValidation.Failed("Password should contains 6 char")
    }

    return RegisterValidation.Success
}

fun validationFirstName(firstName: String): RegisterValidation{
    if (firstName.isEmpty()){
        return RegisterValidation.Failed("FirstName cannot be empty")
    }
    return RegisterValidation.Success
}

fun validationLastName(lastName: String): RegisterValidation{
    if (lastName.isEmpty()){
        return RegisterValidation.Failed("FirstLast cannot be empty")
    }
    return RegisterValidation.Success
}