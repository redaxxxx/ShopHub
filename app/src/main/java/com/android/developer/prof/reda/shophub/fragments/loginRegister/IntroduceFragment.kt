package com.android.developer.prof.reda.shophub.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.activities.ShoppingActivity
import com.android.developer.prof.reda.shophub.databinding.FragmentIntroduceBinding
import com.android.developer.prof.reda.shophub.util.ACCOUNT_OPTIONS_FRAGMENT
import com.android.developer.prof.reda.shophub.util.SHOPPING_ACTIVITY
import com.android.developer.prof.reda.shophub.viewmodel.IntroductionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroduceFragment : Fragment() {

    private lateinit var binding: FragmentIntroduceBinding
    private val viewModel by viewModels<IntroductionViewModel>()
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
                viewModel.startClickButton()
            }
        }

        lifecycleScope.launch {
            viewModel.navigate.collect{
                when(it){
                    SHOPPING_ACTIVITY ->{
                        Intent(requireActivity(), ShoppingActivity::class.java).also {intent->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    ACCOUNT_OPTIONS_FRAGMENT ->{
                        findNavController().navigate(it)
                    }
                }
            }
        }
    }
}

