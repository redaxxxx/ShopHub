package com.android.developer.prof.reda.shophub.fragments.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.data.Category
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.viewmodel.CategoryViewModel
import com.android.developer.prof.reda.shophub.viewmodel.CategoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FurnitureFragment : BaseCategoryFragment() {

    @Inject
    lateinit var firestore: FirebaseFirestore
    private val viewModel by viewModels<CategoryViewModel> {
        CategoryViewModelFactory(firestore, Category.Furniture)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.offerProduct.collect{
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        offerAdapter.submitList(it.data)
                    }
                    is Resource.Error ->{
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.bestProduct.collect{
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        productAdapter.submitList(it.data)
                    }
                    is Resource.Error ->{
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

    }
}