package com.android.developer.prof.reda.shophub.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.adapters.ProductOrderAdapter
import com.android.developer.prof.reda.shophub.data.order.Order
import com.android.developer.prof.reda.shophub.databinding.FragmentOrderDetailsBinding
import com.android.developer.prof.reda.shophub.viewmodel.OrderViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class OrderDetailsFragment : Fragment() {

    private lateinit var binding: FragmentOrderDetailsBinding
    private val adapter by lazy { ProductOrderAdapter() }

    private var order: Order ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        order = OrderDetailsFragmentArgs.fromBundle(requireArguments()).order
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_order_details,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvOrderProducts.adapter = adapter
        adapter.submitList(order?.products)

        //order items details
        binding.tvDateShipping.text = order?.date
        binding.tvOrderStatus.text = order?.orderStatus
        binding.tvAddress.text = order?.address?.address

        //payment details of orders
        binding.tvItemsPrice.text = order?.totalPrice.toString()

        val orderWithCharges = order?.totalPrice?.minus(50)
        binding.tvTotalPrice.text = orderWithCharges.toString()
    }

}