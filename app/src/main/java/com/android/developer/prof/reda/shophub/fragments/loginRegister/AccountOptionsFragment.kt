package com.android.developer.prof.reda.shophub.fragments.loginRegister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.databinding.FragmentAccountOptionsBinding

class AccountOptionsFragment : Fragment() {
    private lateinit var binding: FragmentAccountOptionsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_account_options,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            loginAccountOptionsBtn.setOnClickListener {
                findNavController().navigate(R.id.action_accountOptionsFragment_to_loginFragment)
            }

            registerAccountOptionsBtn.setOnClickListener {
                findNavController().navigate(R.id.action_accountOptionsFragment_to_registerFragment)
            }
        }
    }

}