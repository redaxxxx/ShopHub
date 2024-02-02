package com.android.developer.prof.reda.shophub.fragments.loginRegister

import android.content.Intent
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
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.activities.ShoppingActivity
import com.android.developer.prof.reda.shophub.databinding.FragmentLoginBinding
import com.android.developer.prof.reda.shophub.util.LoginValidation
import com.android.developer.prof.reda.shophub.util.RegisterValidation
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "LoginFragment"
@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            LoginAccountBtn.setOnClickListener {
                val email = etEmailLogin.text.toString()
                val password = etPasswordLogin.text.toString()
                viewModel.login(email, password)
            }

            loginOptionTv.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }

        lifecycleScope.launch {
            viewModel.login.collect{ it ->
                when(it){
                    is Resource.Loading-> {
                        Log.d(TAG, "It Login")
                    }
                    is Resource.Success-> {
                        Log.d(TAG, it.message.toString())
                        Intent(requireActivity(), ShoppingActivity::class.java).also {intent->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }

                    }
                    is Resource.Error-> {
                        Log.d(TAG, it.message.toString())
                        Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.validation.collect{validation->
                if (validation.email is LoginValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.etEmailLogin.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                if (validation.password is LoginValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.etPasswordLogin.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
            }
        }
    }
}