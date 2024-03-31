package com.android.developer.prof.reda.shophub.fragments.home

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.data.Address
import com.android.developer.prof.reda.shophub.databinding.FragmentAddressBinding
import com.android.developer.prof.reda.shophub.util.AddressValidation
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.util.validationCity
import com.android.developer.prof.reda.shophub.viewmodel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
private const val TAG = "AddressFragment"
@AndroidEntryPoint
class AddressFragment : Fragment() {
    private lateinit var binding: FragmentAddressBinding
    private val viewModel by viewModels<AddressViewModel>()
    private var isFirstTime = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isFirstTime = AddressFragmentArgs.fromBundle(requireArguments()).firstTime
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

        binding.saveAddresstButton.setOnClickListener {
            viewModel.addNewAddress(
                Address(
                    binding.firstNameET.text.toString(),
                    binding.familyNameET.text.toString(),
                    binding.phoneNumberET.text.toString(),
                    binding.anotherPhoneNumberET.text.toString(),
                    binding.addressET.text.toString(),
                    binding.moreAddressDetailsET.text.toString(),
                    binding.stateEt.text.toString(),
                    binding.cityEt.text.toString()
                )
            )
            if (isFirstTime){
                findNavController().navigate(R.id.action_addressFragment_to_billingFragment)
            }else{
                findNavController().navigate(R.id.action_addressFragment_to_chooseAddressFragment)
            }
        }

        binding.addressCancelButton.setOnClickListener {
            findNavController().navigate(R.id.action_addressFragment_to_chooseAddressFragment)
        }
        lifecycleScope.launch {
            viewModel.addNewAddress.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.saveAddresstButton.startAnimation()
                    }
                    is Resource.Success->{
                        binding.saveAddresstButton.revertAnimation()

                        binding.firstNameET.text?.clear()
                        binding.familyNameET.text?.clear()
                        binding.phoneNumberET.text?.clear()
                        binding.anotherPhoneNumberET.text?.clear()
                        binding.addressET.text?.clear()
                        binding.moreAddressDetailsET.text?.clear()
                        binding.stateEt.text?.clear()
                        binding.cityEt.text?.clear()
                    }
                    is Resource.Error->{
                        binding.saveAddresstButton.stopAnimation()
                        Toast.makeText(requireActivity(), "${it.message}", Toast.LENGTH_LONG).show()
                        Log.d(TAG, it.message.toString())
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.validation.collect {
                if (it.firstName is AddressValidation.Failed){
                    binding.firstNameOutline.apply {
                        requestFocus()
                        error = it.firstName.message
                    }
                }
                if (it.familyName is AddressValidation.Failed){
                    binding.familyNameOutline.apply {
                        requestFocus()
                        error = it.familyName.message
                    }
                }
                if (it.phoneNumber is AddressValidation.Failed){
                    binding.phoneNumberOutline.apply {
                        requestFocus()
                        error = it.phoneNumber.message
                    }
                }

                if (it.address is AddressValidation.Failed){
                    binding.addressOutline.apply {
                        requestFocus()
                        error = it.address.message
                    }
                }
                if (it.state is AddressValidation.Failed){
                    binding.stateOutline.apply {
                        requestFocus()
                        error = it.state.message
                    }
                }
                if (it.city is AddressValidation.Failed){
                    binding.cityOutline2.apply {
                        requestFocus()
                        error = it.city.message
                    }
                }

            }
        }
    }

}