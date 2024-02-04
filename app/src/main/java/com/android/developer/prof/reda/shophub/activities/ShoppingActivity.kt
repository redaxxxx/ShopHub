package com.android.developer.prof.reda.shophub.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.databinding.ActivityShoppingBinding

class ShoppingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityShoppingBinding>(this, R.layout.activity_shopping)

        val navController by lazy {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
            navHostFragment.navController
        }

        binding.bottomNavView.setupWithNavController(navController)
    }
}