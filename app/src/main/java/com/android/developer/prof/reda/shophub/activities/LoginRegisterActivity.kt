package com.android.developer.prof.reda.shophub.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.developer.prof.reda.shophub.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
    }
}