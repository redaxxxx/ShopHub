package com.android.developer.prof.reda.shophub.fragments.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.databinding.FragmentAccountBinding
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.viewmodel.AccountViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "AccountFragment"

@AndroidEntryPoint
class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private val viewModel by viewModels<AccountViewModel>()
    private var imageUri: Uri? = null

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
            R.layout.fragment_account,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.userInfo.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                    }

                    is Resource.Success -> {
                        binding.usernameTextView.text = "${it.data?.firstName} ${it.data?.lastName}"
                        binding.emailTextView.text = it.data?.email

                        Glide.with(requireView())
                            .load(it.data?.imagePath)
                            .into(binding.profileImageView)
                        binding.profileImageView.borderColor = resources.getColor(R.color.g_blue)
                    }

                    is Resource.Error -> {
                        Log.d(TAG, "Error ${it.message.toString()}")
                    }

                    else -> Unit
                }
            }
        }


        binding.editProfileImageButton.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToEditProfileFragment())
        }

        binding.addressProfileSection.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToAddressFragment())
        }

        binding.orderProfileSection.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToOrderFragment())
        }

        binding.paymentProfileSection.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToPaymentFragment())
        }

        binding.myFavoriteProfileSection.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToFavoriteProductsFragment())
        }

        binding.settingsSection.setOnClickListener {
        }

        binding.logoutSection.setOnClickListener {

        }
    }
}