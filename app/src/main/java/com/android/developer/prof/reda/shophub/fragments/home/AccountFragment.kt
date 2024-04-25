package com.android.developer.prof.reda.shophub.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding

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

        binding.addressProfileSection.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToAddressFragment())
        }

        binding.orderProfileSection.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToOrderFragment())
        }

        binding.paymentProfileSection.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToPaymentFragment())
        }

        binding.profileSection.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToProfileFragment())
        }

        binding.myFavoriteProfileSection.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToFavoriteProductsFragment())
        }
    }
}