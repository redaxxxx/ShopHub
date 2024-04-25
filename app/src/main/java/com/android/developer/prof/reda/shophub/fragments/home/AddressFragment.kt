package com.android.developer.prof.reda.shophub.fragments.home

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
import com.android.developer.prof.reda.shophub.adapters.AddressAdapter
import com.android.developer.prof.reda.shophub.databinding.FragmentAddressBinding
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.viewmodel.AddressViewModel
import com.android.developer.prof.reda.shophub.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "ChooseAddressFragment"
@AndroidEntryPoint
class AddressFragment : Fragment() {
    private lateinit var binding: FragmentAddressBinding
    private val viewModel by viewModels<AddressViewModel>()
    private val adapter by lazy { AddressAdapter() }
    private val sharedViewModel: SharedViewModel by navGraphViewModels(R.id.shopping_graph)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_address,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addressesRv.adapter = adapter

        adapter.onAddressClick = {address->
            sharedViewModel.setAddressInfo(address)
        }

        adapter.onEditClick = {
            sharedViewModel.setAddressInfo(it)
//            Log.d(TAG, "Address Document ID: ${viewModel.getDocumentId(it)}")
            findNavController().navigate(AddressFragmentDirections.actionAddressFragmentToAddAddressFragment(true))
        }

        binding.addNewAddressBtn.setOnClickListener {
            findNavController().navigate(AddressFragmentDirections.actionAddressFragmentToAddAddressFragment(false))
        }

        binding.chooseAddressBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        lifecycleScope.launch {
            viewModel.addresses.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        Log.d(TAG, "Loading!!!")
                    }
                    is Resource.Success ->{
                        adapter.submitList(it.data)
                        Log.d(TAG, "Addresses: ${it.data}")
                    }
                    is Resource.Error->{
                        Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG).show()
                        Log.d(TAG, it.message.toString())
                    }
                    else -> Unit
                }
            }
        }
    }

}