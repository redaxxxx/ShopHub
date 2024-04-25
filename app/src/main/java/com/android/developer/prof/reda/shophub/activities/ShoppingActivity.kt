package com.android.developer.prof.reda.shophub.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.databinding.ActivityShoppingBinding
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    val viewModel by viewModels<CartViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityShoppingBinding>(this, R.layout.activity_shopping)

        val navController by lazy {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
            navHostFragment.navController
        }

        binding.bottomNavView.setupWithNavController(navController)

        lifecycleScope.launch {
            viewModel.cartProducts.collectLatest {
                when(it){
                    is Resource.Success ->{
                        val count = it.data?.size ?: 0
                        binding.bottomNavView.getOrCreateBadge(R.id.cartFragment).apply {
                            number = count
                            backgroundColor = resources.getColor(R.color.g_blue)
                        }
                    }
                    else -> Unit
                }
            }
        }

        navController.addOnDestinationChangedListener{_, destination, _ ->
            if (destination.id == R.id.homeFragment || destination.id == R.id.cartFragment ||
                destination.id == R.id.accountFragment || destination.id == R.id.searchFragment){
                binding.bottomNavView.visibility = View.VISIBLE
            } else{
                binding.bottomNavView.visibility = View.GONE
            }
        }
    }
}