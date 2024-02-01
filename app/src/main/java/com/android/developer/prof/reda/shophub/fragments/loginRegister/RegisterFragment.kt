package com.android.developer.prof.reda.shophub.fragments.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.data.User
import com.android.developer.prof.reda.shophub.databinding.FragmentRegisterBinding
import com.android.developer.prof.reda.shophub.util.RegisterValidation
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private val viewModel by viewModels<RegisterViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_register,
            container,
            false
        )


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            registerAccountBtn.setOnClickListener {
                val user = User(
                    etFirstNameRegister.text.toString().trim(),
                    etLastNameRegister.text.toString().trim(),
                    etEmailRegister.text.toString().trim()
                )
                val password = etPasswordRegister.text.toString()

                viewModel.createAccountWithEmailAndPassword(user, password)
            }
        }

        lifecycleScope.launch {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading ->{
                        Log.d(TAG, "It Register")
                    }
                    is Resource.Success ->{
                        Log.d(TAG, it.message.toString())
                    }
                    is Resource.Error ->{
                        Log.d(TAG, it.message.toString())
                    }

                    else -> {
                        Log.d(TAG, "Error")
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.validation.collect{validation->
                if (validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.etEmailRegister.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                if (validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.etPasswordRegister.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
                if (validation.firstName is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.etFirstNameRegister.apply {
                            requestFocus()
                            error = validation.firstName.message
                        }
                    }
                }
                if (validation.lastName is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.etLastNameRegister.apply {
                            requestFocus()
                            error = validation.lastName.message
                        }
                    }
                }
            }
        }
    }
}