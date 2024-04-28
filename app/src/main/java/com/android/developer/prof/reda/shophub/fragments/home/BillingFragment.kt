package com.android.developer.prof.reda.shophub.fragments.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.adapters.BillingProductAdapter
import com.android.developer.prof.reda.shophub.data.Address
import com.android.developer.prof.reda.shophub.data.CartProduct
import com.android.developer.prof.reda.shophub.data.order.Order
import com.android.developer.prof.reda.shophub.data.order.OrderStatus
import com.android.developer.prof.reda.shophub.databinding.FragmentBillingBinding
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.viewmodel.OrderViewModel
import com.android.developer.prof.reda.shophub.viewmodel.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.UUID

private const val TAG = "BillingFragment"

@AndroidEntryPoint
class BillingFragment : Fragment() {
    private lateinit var binding: FragmentBillingBinding
    private val adapter by lazy { BillingProductAdapter() }
    private var products = emptyList<CartProduct>()
    private var totalPrice = 0f
    private val sharedViewModel: SharedViewModel by navGraphViewModels(R.id.shopping_graph)
    private var selectedAddress: Address ?= null
    private val orderViewModel by viewModels<OrderViewModel>()

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

        if (selectedAddress == null){
            binding.tvBuyerName.text = "Please select your Address"
            binding.tvBuyerAddress.visibility = View.GONE
        }

        binding.tvEditAddress.setOnClickListener {
//            sharedViewModel.setOrderInfo(products, totalPrice)
            findNavController().navigate(BillingFragmentDirections.actionBillingFragmentToAddressFragment())
        }

        lifecycleScope.launch {
            sharedViewModel.addressOrder.collectLatest {
                when(it){
                    is Resource.Success ->{
                        binding.tvBuyerName.text = "${it.data?.firstName} ${it.data?.familyName}"
                        binding.tvBuyerAddress.text = "${it.data?.address}"
                        selectedAddress = it.data
                    }
                    is Resource.Error ->{
                        Log.d(TAG, it.message.toString())
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            orderViewModel.order.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.placeOrder.startAnimation()
                    }
                    is Resource.Success ->{
                        binding.placeOrder.revertAnimation()
                        findNavController().navigateUp()
                        Snackbar.make(requireView(), "Your order was Placed", Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Error ->{
                        Log.d(TAG, it.message.toString())
                        Toast.makeText(requireContext(), "Error ${it.message.toString()}", Toast.LENGTH_LONG)
                            .show()
                    }
                    else -> Unit
                }
            }
        }

        binding.tvTotalPrice.text = "$ $totalPrice"

        binding.placeOrder.setOnClickListener {
            showOrderConfirmationDialog()
        }
    }

    private fun showOrderConfirmationDialog(){
        val simpleDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = simpleDate.format(Date())

        val alertDialog = AlertDialog.Builder(requireActivity()).apply {
            setTitle("Place Order Items")
            setMessage("Do you want to Order your items?")
            setNegativeButton("Cancel"){dialog,_->
                dialog.dismiss()
            }
            setPositiveButton("Yes"){dialog,_->
                orderViewModel.placedOrder(
                    Order(
                        UUID.randomUUID().toString(),
                        OrderStatus.Ordered.status,
                        totalPrice,
                        products,
                        selectedAddress!!,
                        currentDate
                    )
                )
                dialog.dismiss()
            }
        }
        alertDialog.create()
        alertDialog.show()
    }
}