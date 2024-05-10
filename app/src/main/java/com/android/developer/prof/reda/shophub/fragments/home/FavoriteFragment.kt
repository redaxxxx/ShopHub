package com.android.developer.prof.reda.shophub.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.adapters.FavoriteAdapter
import com.android.developer.prof.reda.shophub.databinding.FragmentFavoriteBinding
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.viewmodel.DetailsViewModel
import com.android.developer.prof.reda.shophub.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "FavoriteFragment"
@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel by viewModels<FavoriteViewModel>()
    private val detailViewModel by viewModels<DetailsViewModel>()
    private val adapter by lazy { FavoriteAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_favorite,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.onDelete = {
            viewModel.deleteFavoriteProduct(it)
        }

        adapter.onDetailsProduct = {
            findNavController().navigate(FavoriteFragmentDirections.actionFavoriteFragmentToProductDetailsFragment(it))
        }

        lifecycleScope.launch {
            viewModel.favoriteProduct.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        Log.d(TAG, "Loading!!!")
                    }
                    is Resource.Success ->{
                        adapter.submitList(it.data)
                        binding.rvFavoriteProduct.adapter = adapter
                    }
                    is Resource.Error ->{
                        Log.d(TAG, "Error: ${it.message.toString()}")
                    }
                    else -> Unit
                }
            }
        }
    }

}