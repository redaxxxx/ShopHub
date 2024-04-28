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
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.adapters.CartProductsAdapter
import com.android.developer.prof.reda.shophub.data.CartProduct
import com.android.developer.prof.reda.shophub.databinding.FragmentCartBinding
import com.android.developer.prof.reda.shophub.firebase.FirebaseCommon
import com.android.developer.prof.reda.shophub.util.ACCOUNT_OPTIONS_FRAGMENT
import com.android.developer.prof.reda.shophub.util.ADD_ADDRESS_FRAGMENT
import com.android.developer.prof.reda.shophub.util.BILLING_FRAGMENT
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.util.SHOPPING_ACTIVITY
import com.android.developer.prof.reda.shophub.viewmodel.CartViewModel
import com.android.developer.prof.reda.shophub.viewmodel.IntroductionViewModel
import com.android.developer.prof.reda.shophub.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
private const val TAG = "CartFragment"
@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val viewModel by viewModels<CartViewModel>()
    private var products = emptyList<CartProduct>()
    private var totalPrice: Float = 0f
    private val adapter by lazy { CartProductsAdapter(requireActivity()) }

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
                    if (price != null) {
                        totalPrice = price
                    }
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
                        if (it.data?.isEmpty() == true){
                            showEmptyCart()
                        }else{
                            hideEmptyCart()
                            val count = it.data?.size ?: 0
                            binding.itemNumberInCart.text = "(${count})"
                            binding.progressBarCart.visibility = View.INVISIBLE
                            adapter.submitList(it.data)
                            products = it.data!!
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

        lifecycleScope.launch {
            viewModel.navigate.collectLatest {
                when(it){
                    BILLING_FRAGMENT-> {
                        binding.checkoutButton.setOnClickListener {
//                            sharedViewModel.setOrderInfo(products, totalPrice)
                            findNavController().navigate(
                                CartFragmentDirections.actionCartFragmentToBillingFragment(
                                    products.toTypedArray(),
                                    totalPrice
                                )
                            )
                        }
                    }
                    ADD_ADDRESS_FRAGMENT-> {
                        binding.checkoutButton.setOnClickListener {
                            findNavController().navigate(
                                CartFragmentDirections.actionCartFragmentToAddAddressFragment(false)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun hideEmptyCart() {
        binding.apply {
            emptyConstraintLayout.visibility = View.GONE
//            rvCart.visibility = View.VISIBLE
//            progressBarCart.visibility = View.VISIBLE
//            totalConstraintLayout.visibility = View.VISIBLE
            constraintLayout.visibility = View.VISIBLE
        }
    }
    private fun showEmptyCart() {
        binding.apply {
            emptyConstraintLayout.visibility = View.VISIBLE
//            rvCart.visibility = View.GONE
//            progressBarCart.visibility = View.GONE
//            totalConstraintLayout.visibility = View.GONE
            constraintLayout.visibility = View.GONE
        }
    }

}