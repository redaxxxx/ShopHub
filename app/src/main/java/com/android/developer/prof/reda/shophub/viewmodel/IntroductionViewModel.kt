package com.android.developer.prof.reda.shophub.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.util.ACCOUNT_OPTIONS_FRAGMENT
import com.android.developer.prof.reda.shophub.util.INTRODUCTION_KEY
import com.android.developer.prof.reda.shophub.util.SHOPPING_ACTIVITY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val _navigate = MutableStateFlow(0)

    val navigate: StateFlow<Int>
        get() = _navigate

    companion object{
    }

    init {
        val isButtonClicked = sharedPreferences.getBoolean(INTRODUCTION_KEY, false)
        val user = firebaseAuth.currentUser

        if (user != null){
            viewModelScope.launch {
                _navigate.emit(SHOPPING_ACTIVITY)
            }
        }else if (isButtonClicked){
            viewModelScope.launch {
                _navigate.emit(ACCOUNT_OPTIONS_FRAGMENT)
            }
        }else {
            Unit
        }
    }

    fun startClickButton(){
        sharedPreferences.edit().putBoolean(INTRODUCTION_KEY, true).apply()
    }
}