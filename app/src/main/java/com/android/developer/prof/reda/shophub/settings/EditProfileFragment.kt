package com.android.developer.prof.reda.shophub.settings

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.data.User
import com.android.developer.prof.reda.shophub.databinding.FragmentEditProfileBinding
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.viewmodel.AccountViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "EditProfileFragment"
@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel by viewModels<AccountViewModel>()
    private var imageUri: Uri?= null
    private val getImage: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            imageUri = it
        }
    private var email: String = ""
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
            R.layout.fragment_edit_profile,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.userInfo.collectLatest {
                when(it){
                    is Resource.Loading-> {
                    }
                    is Resource.Success->{
                        Glide.with(requireContext())
                            .load(it.data?.imagePath)
                            .into(binding.editProfileImageView)

                        binding.profileFirstNameET.setText(it.data?.firstName)
                        binding.profileLastNameET.setText(it.data?.lastName)
                        email = it.data?.email.toString()
                    }
                    is Resource.Error->{
                        Log.d(TAG, "Error ${it.message.toString()}")
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.updateUserInfo.collectLatest {
                when(it){
                    is Resource.Loading-> {
                        binding.saveEditProfileButton.startAnimation()
                    }
                    is Resource.Success->{
                        binding.saveEditProfileButton.revertAnimation()
                        findNavController().navigateUp()
                    }
                    is Resource.Error->{
                        binding.saveEditProfileButton.revertAnimation()
                        Log.d(TAG, "Error ${it.message.toString()}")
                    }
                    else -> Unit
                }
            }
        }

        binding.editImageProfileButton.setOnClickListener {
            getImage.launch("image/*")
        }

        binding.saveEditProfileButton.setOnClickListener {

            viewModel.updateUserImg(
                User(binding.profileFirstNameET.text.toString(),
                    binding.profileLastNameET.text.toString(),
                    email),
                imageUri!!
            )
        }
    }
}