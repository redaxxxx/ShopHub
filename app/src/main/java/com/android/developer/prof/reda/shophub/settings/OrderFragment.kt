package com.android.developer.prof.reda.shophub.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.adapters.OrderAdapter
import com.android.developer.prof.reda.shophub.databinding.FragmentOrderBinding
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "OrderFragment"

@AndroidEntryPoint
class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private val viewModel by viewModels<OrderViewModel>()
    private val adapter by lazy { OrderAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_order,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvOrders.adapter = adapter

        lifecycleScope.launch {
            viewModel.orders.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.progressBarOrders.visibility = View.VISIBLE
                    }

                    is Resource.Success ->{
                        binding.progressBarOrders.visibility = View.GONE
                        adapter.submitList(it.data)
                    }
                    is Resource.Error ->{
                        binding.progressBarOrders.visibility = View.GONE
                        Log.d(TAG, "${it.message.toString()}")
                    }
                    else -> Unit
                }
            }
        }
    }
}