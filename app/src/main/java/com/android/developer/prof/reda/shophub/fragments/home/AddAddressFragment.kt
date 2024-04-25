package com.android.developer.prof.reda.shophub.fragments.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.data.Address
import com.android.developer.prof.reda.shophub.databinding.FragmentAddAddressBinding
import com.android.developer.prof.reda.shophub.databinding.FragmentAddressBinding
import com.android.developer.prof.reda.shophub.util.AddressValidation
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.viewmodel.AddAddressViewModel
import com.android.developer.prof.reda.shophub.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID

private const val TAG = "AddressFragment"

@AndroidEntryPoint
class AddAddressFragment : Fragment() {
    private lateinit var binding: FragmentAddAddressBinding
    private val viewModel by viewModels<AddAddressViewModel>()
    private val sharedViewModel: SharedViewModel by navGraphViewModels(R.id.shopping_graph)
    private var editAddress: Address? = null
    private var isEditingAddress = false
    private var addressHasChanged: Boolean = false
    private var mOnTouchListener = OnTouchListener { _, _ ->
        addressHasChanged = true
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        address = AddressFragmentArgs.fromBundle(requireArguments()).address!!
        isEditingAddress = AddAddressFragmentArgs.fromBundle(requireArguments()).isEditingAddress
        Log.d(TAG, "is Editing Address: $isEditingAddress")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_address,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isEditingAddress) {

            lifecycleScope.launch {
                sharedViewModel.addressOrder.collectLatest {
                    when (it) {
                        is Resource.Success -> {
                            editAddress = it.data
                        }

                        is Resource.Error -> {
                            Log.d(TAG, it.message.toString())
                        }

                        else -> Unit
                    }
                }
            }

            binding.firstNameET.setText(editAddress?.firstName)
            binding.familyNameET.setText(editAddress?.familyName)
            binding.countryNameET.setText(editAddress?.country)
            binding.addressET.setText(editAddress?.address)
            binding.moreAddressDetailsET.setText(editAddress?.moreAddressDetails)
            binding.phoneNumberET.setText(editAddress?.phoneNumber)
            binding.anotherPhoneNumberET.setText(editAddress?.anotherPhoneNumber)
            binding.stateEt.setText(editAddress?.state)
            binding.cityEt.setText(editAddress?.city)
            binding.postalCodeEt.setText(editAddress?.postalCode.toString())


//            binding.saveAddresstButton.setOnClickListener {
//                viewModel.updateAddress(
//                    Address(
//                        editAddress!!.id,
//                        binding.firstNameET.text.toString(),
//                        binding.familyNameET.text.toString(),
//                        binding.countryNameET.text.toString(),
//                        binding.phoneNumberET.text.toString(),
//                        binding.anotherPhoneNumberET.text.toString(),
//                        binding.addressET.text.toString(),
//                        binding.moreAddressDetailsET.text.toString(),
//                        binding.stateEt.text.toString(),
//                        binding.cityEt.text.toString(),
//                        binding.postalCodeEt.text.toString().toInt()
//                    )
//                )
//            }
        }


        binding.saveAddresstButton.setOnClickListener {
            if (isEditingAddress){
                viewModel.updateAddress(
                    Address(
                        editAddress!!.id,
                        binding.firstNameET.text.toString(),
                        binding.familyNameET.text.toString(),
                        binding.countryNameET.text.toString(),
                        binding.phoneNumberET.text.toString(),
                        binding.anotherPhoneNumberET.text.toString(),
                        binding.addressET.text.toString(),
                        binding.moreAddressDetailsET.text.toString(),
                        binding.stateEt.text.toString(),
                        binding.cityEt.text.toString(),
                        binding.postalCodeEt.text.toString().toInt()
                    )
                )
            }else{
                viewModel.addNewAddress(
                    Address(
                        UUID.randomUUID().toString(),
                        binding.firstNameET.text.toString(),
                        binding.familyNameET.text.toString(),
                        binding.countryNameET.text.toString(),
                        binding.phoneNumberET.text.toString(),
                        binding.anotherPhoneNumberET.text.toString(),
                        binding.addressET.text.toString(),
                        binding.moreAddressDetailsET.text.toString(),
                        binding.stateEt.text.toString(),
                        binding.cityEt.text.toString(),
                        binding.postalCodeEt.text.toString().toInt()
                    )
                )
            }
        }

        binding.addressCancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

        lifecycleScope.launch {
            viewModel.addNewAddress.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.saveAddresstButton.startAnimation()
                    }

                    is Resource.Success -> {
                        findNavController().navigateUp()

                        binding.firstNameET.text?.clear()
                        binding.familyNameET.text?.clear()
                        binding.countryNameET.text?.clear()
                        binding.phoneNumberET.text?.clear()
                        binding.anotherPhoneNumberET.text?.clear()
                        binding.addressET.text?.clear()
                        binding.moreAddressDetailsET.text?.clear()
                        binding.stateEt.text?.clear()
                        binding.cityEt.text?.clear()
                    }

                    is Resource.Error -> {
                        binding.saveAddresstButton.stopAnimation()
                        Toast.makeText(requireActivity(), "${it.message}", Toast.LENGTH_LONG).show()
                        Log.d(TAG, it.message.toString())
                    }

                    else -> Unit
                }
            }
        }
        lifecycleScope.launch {
            viewModel.updatedAddress.collectLatest {
                when(it){
                    is Resource.Success ->{
                        Log.d(TAG, "Address ID: ${it.data?.id}")
                        findNavController().navigate(AddAddressFragmentDirections.actionAddAddressFragmentToAddressFragment())
                    }
                    is Resource.Error ->{
                        Log.d(TAG, it.message.toString())
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.validation.collect {
                if (it.firstName is AddressValidation.Failed) {
                    binding.firstNameOutline.apply {
                        requestFocus()
                        error = it.firstName.message
                    }
                }
                if (it.familyName is AddressValidation.Failed) {
                    binding.familyNameOutline.apply {
                        requestFocus()
                        error = it.familyName.message
                    }
                }
                if (it.phoneNumber is AddressValidation.Failed) {
                    binding.phoneNumberOutline.apply {
                        requestFocus()
                        error = it.phoneNumber.message
                    }
                }

                if (it.address is AddressValidation.Failed) {
                    binding.addressOutline.apply {
                        requestFocus()
                        error = it.address.message
                    }
                }
                if (it.state is AddressValidation.Failed) {
                    binding.stateOutline.apply {
                        requestFocus()
                        error = it.state.message
                    }
                }
                if (it.city is AddressValidation.Failed) {
                    binding.cityOutline2.apply {
                        requestFocus()
                        error = it.city.message
                    }
                }

            }
        }


        binding.postalCodeEt.setOnTouchListener(mOnTouchListener)
        binding.firstNameET.setOnTouchListener(mOnTouchListener)
        binding.familyNameET.setOnTouchListener(mOnTouchListener)
        binding.countryNameET.setOnTouchListener(mOnTouchListener)
        binding.phoneNumberET.setOnTouchListener(mOnTouchListener)
        binding.anotherPhoneNumberET.setOnTouchListener(mOnTouchListener)
        binding.addressET.setOnTouchListener(mOnTouchListener)
        binding.moreAddressDetailsET.setOnTouchListener(mOnTouchListener)
        binding.cityEt.setOnTouchListener(mOnTouchListener)
        binding.stateEt.setOnTouchListener(mOnTouchListener)
    }

    private fun showUnSavedChangesDialog(discardButtonCLickListener: DialogInterface.OnClickListener) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Discard your change and quit editing")
        dialogBuilder.setPositiveButton("Discard", discardButtonCLickListener)
        dialogBuilder.setNegativeButton("Keep Editing") { dialog, which ->
            dialog?.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()

        requireView().setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                val dialogInterface = DialogInterface.OnClickListener { dialog, which ->
                    findNavController().navigateUp()
                }
                showUnSavedChangesDialog(dialogInterface)
            }
            true
        }
    }
}