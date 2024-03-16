package com.android.developer.prof.reda.shophub.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.adapters.CartProductsAdapter
import com.android.developer.prof.reda.shophub.databinding.FragmentCartBinding
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val viewModel by viewModels<CartViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_cart,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CartProductsAdapter(requireActivity())
        binding.rvCart.adapter = adapter

        lifecycleScope.launch {
            viewModel.cartProducts.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.progressBarCart.show()
                    }
                    is Resource.Success ->{
                        adapter.submitList(it.data)
                        val count = it.data?.size ?: 0
                        binding.itemNumberInCart.text = "(${count})"
                        binding.progressBarCart.hide()
                    }
                    is Resource.Error ->{
                        Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG).show()
                        binding.progressBarCart.hide()
                    }
                    else -> Unit
                }
            }
        }
    }

}