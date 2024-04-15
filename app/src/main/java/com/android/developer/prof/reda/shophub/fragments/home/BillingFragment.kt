package com.android.developer.prof.reda.shophub.fragments.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.adapters.BillingProductAdapter
import com.android.developer.prof.reda.shophub.data.Address
import com.android.developer.prof.reda.shophub.data.CartProduct
import com.android.developer.prof.reda.shophub.databinding.FragmentBillingBinding
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.viewmodel.BillingViewModel
import com.android.developer.prof.reda.shophub.viewmodel.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "BillingFragment"

@AndroidEntryPoint
class BillingFragment : Fragment() {
    private lateinit var binding: FragmentBillingBinding
    private val adapter by lazy { BillingProductAdapter() }
    private val viewModel by viewModels<BillingViewModel>()
    private var products = emptyList<CartProduct>()
    private var totalPrice = 0f
    private val sharedViewModel: SharedViewModel by navGraphViewModels(R.id.shopping_graph)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        products = BillingFragmentArgs.fromBundle(requireArguments()).products.toList()
        totalPrice = BillingFragmentArgs.fromBundle(requireArguments()).totalPrice
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_billing,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.productBillingRv.adapter = adapter
        adapter.submitList(products)

        binding.tvEditAddress.setOnClickListener {
//            sharedViewModel.setOrderInfo(products, totalPrice)
            findNavController().navigate(BillingFragmentDirections.actionBillingFragmentToChooseAddressFragment())
        }

        lifecycleScope.launch {
            sharedViewModel.addressOrder.collectLatest {
                when(it){
                    is Resource.Success ->{
                        binding.tvBuyerName.text = "${it.data?.firstName} ${it.data?.familyName}"
                        binding.tvBuyerAddress.text = "${it.data?.address}"
                    }
                    is Resource.Error ->{
                        Log.d(TAG, it.message.toString())
                    }
                    else -> Unit
                }
            }
        }

        binding.tvTotalPrice.text = "$ $totalPrice"

//        val selectedAddress = requireArguments().getParcelable<Address>("address")
//
    }
}