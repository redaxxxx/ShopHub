package com.android.developer.prof.reda.shophub.fragments.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
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
import com.android.developer.prof.reda.shophub.firebase.FirebaseCommon
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
private const val TAG = "CartFragment"
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

        adapter.onPlusClick = {
            viewModel.quantityChanging(it, FirebaseCommon.QuantityChanging.INCREASE)
        }
        adapter.onMinusClick = {
            viewModel.quantityChanging(it, FirebaseCommon.QuantityChanging.DECREASE)
        }

        lifecycleScope.launch {
            viewModel.deleteDialog.collectLatest {
                val alertDialog = AlertDialog.Builder(requireActivity()).apply {
                    setTitle("Delete item from cart")
                    setMessage("Do you want to delete item from your cart?")
                    setNegativeButton("Cancel"){dialog,_->
                        dialog.dismiss()
                    }
                    setPositiveButton("Delete"){dialog,_->
                        viewModel.deleteItemFromCart(it)
                        dialog.dismiss()
                    }
                }
                alertDialog.create()
                alertDialog.show()
            }
        }

        lifecycleScope.launch {
            viewModel.productsPrice.collectLatest {price->
                price.let {
                    binding.tvTotalPrice.text = "$ $price"
                }
            }
        }

        lifecycleScope.launch {
            viewModel.cartProducts.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.progressBarCart.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        if (it.data == null){
                            showEmptyCart()
                        }else{
                            hideEmptyCart()
                            val count = it.data.size ?: 0
                            binding.itemNumberInCart.text = "(${count})"
                            binding.progressBarCart.visibility = View.INVISIBLE
                            adapter.submitList(it.data)

                        }

                    }
                    is Resource.Error ->{
                        Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG).show()
                        Log.d(TAG, "${it.message}")
                        binding.progressBarCart.visibility = View.INVISIBLE
                    }
                    else -> Unit
                }
            }
        }


    }

    private fun hideEmptyCart() {
        binding.apply {
            cartEmptyLayout.visibility = View.GONE
            rvCart.visibility = View.VISIBLE
            progressBarCart.visibility = View.VISIBLE
            totalConstraintLayout.visibility = View.VISIBLE
        }
    }
    private fun showEmptyCart() {
        binding.apply {
            cartEmptyLayout.visibility = View.VISIBLE
            rvCart.visibility = View.GONE
            progressBarCart.visibility = View.GONE
            totalConstraintLayout.visibility = View.GONE
        }
    }

}