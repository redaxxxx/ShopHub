package com.android.developer.prof.reda.shophub.fragments.home

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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.adapters.AddressAdapter
import com.android.developer.prof.reda.shophub.databinding.FragmentChooseAddressBinding
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.viewmodel.AddressViewModel
import com.android.developer.prof.reda.shophub.viewmodel.ChooseAddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "ChooseAddressFragment"
@AndroidEntryPoint
class ChooseAddressFragment : Fragment() {
    private lateinit var binding: FragmentChooseAddressBinding
    private val viewModel by viewModels<ChooseAddressViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_choose_address,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AddressAdapter(
            AddressAdapter.OnClickAddressListener{
            findNavController().navigate(ChooseAddressFragmentDirections.actionChooseAddressFragmentToBillingFragment(it))
        })

        lifecycleScope.launch {
            viewModel.addresses.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        adapter.submitList(it.data)
                    }
                    is Resource.Error->{
                        Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG)
                        Log.d(TAG, it.message.toString())
                    }
                    else -> Unit
                }
            }
        }
    }

}