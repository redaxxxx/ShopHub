package com.android.developer.prof.reda.shophub.fragments.loginRegister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.databinding.FragmentIntroduceBinding


class IntroduceFragment : Fragment() {

    private lateinit var binding: FragmentIntroduceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_introduce,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            startBtn.setOnClickListener {
                findNavController().navigate(R.id.action_introduceFragment_to_accountOptionsFragment)
            }
        }
    }
}

