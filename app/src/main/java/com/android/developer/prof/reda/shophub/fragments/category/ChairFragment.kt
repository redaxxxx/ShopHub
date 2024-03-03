package com.android.developer.prof.reda.shophub.fragments.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.developer.prof.reda.shophub.data.Category
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.viewmodel.CategoryViewModel
import com.android.developer.prof.reda.shophub.viewmodel.CategoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChairFragment : BaseCategoryFragment() {

    @Inject
    lateinit var firestore: FirebaseFirestore

    private val viewModel by viewModels<CategoryViewModel> {
        CategoryViewModelFactory(firestore, Category.Chair)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.offerProduct.collect{
                when(it){
                    is Resource.Loading -> {
                        showOfferProductLoading()
                    }
                    is Resource.Success ->{
                        offerAdapter.submitList(it.data)
                        hideOfferProductLoading()
                    }
                    is Resource.Error ->{
                        Log.d("ChairFragment", it.message.toString())
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                        hideOfferProductLoading()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.bestProduct.collect{
                when(it){
                    is Resource.Loading -> {
                        showBestProductLoading()
                    }
                    is Resource.Success ->{
                        productAdapter.submitList(it.data)
                        hideBestProductLoading()
                    }
                    is Resource.Error ->{
                        Log.d("ChairFragment", it.message.toString())
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                        hideBestProductLoading()
                    }
                    else -> Unit
                }
            }
        }

    }
}