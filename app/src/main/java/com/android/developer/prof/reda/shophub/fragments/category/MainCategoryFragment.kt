package com.android.developer.prof.reda.shophub.fragments.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout.Spec
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.adapters.BestDealsAdapter
import com.android.developer.prof.reda.shophub.adapters.BestProductAdapter
import com.android.developer.prof.reda.shophub.adapters.SpecialProductAdapter
import com.android.developer.prof.reda.shophub.data.Product
import com.android.developer.prof.reda.shophub.databinding.FragmentMainCategoryBinding
import com.android.developer.prof.reda.shophub.fragments.home.HomeFragmentDirections
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.util.showBottomNavigationView
import com.android.developer.prof.reda.shophub.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MainCategoryFragment"
@AndroidEntryPoint
class MainCategoryFragment : Fragment() {

    private lateinit var binding: FragmentMainCategoryBinding
    private val viewModel by viewModels<MainCategoryViewModel>()
    private lateinit var specialProductAdapter: SpecialProductAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var bestProductAdapter: BestProductAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main_category,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        specialProductAdapter = SpecialProductAdapter(
            SpecialProductAdapter.OnItemClickListener{
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment2(it))

            }
        )
        bestDealsAdapter = BestDealsAdapter(
            BestDealsAdapter.OnItemClickListener{
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment2(it))
            }
        )
        bestProductAdapter = BestProductAdapter( BestProductAdapter.OnItemCLickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment2(it))
        })

        binding.rvSpecialProduct.adapter = specialProductAdapter
        binding.rvBestDeals.adapter = bestDealsAdapter
        binding.rvBestProduct.adapter = bestProductAdapter

        lifecycleScope.launch {
            viewModel.specialProducts.collectLatest {
                when(it){
                    is Resource.Loading -> showLoading()
                    is Resource.Success -> {
                        specialProductAdapter.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.d(TAG, it.message.toString())
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.bestDeals.collectLatest {
                when(it){
                    is Resource.Loading -> showLoading()
                    is Resource.Success -> {
                        bestDealsAdapter.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.d(TAG, it.message.toString())
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.bestProduct.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.bestProductProgressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        bestProductAdapter.submitList(it.data)

                        binding.nestedScrollViewMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
                            if (v.getChildAt(0).bottom <= v.height + scrollY){
                                bestProductAdapter.submitList(it.data)
                                binding.bestProductProgressBar.visibility = View.GONE
                            }
                        })
                    }
                    is Resource.Error -> {
                        binding.bestProductProgressBar.visibility = View.GONE
                        Log.d(TAG, it.message.toString())
                    }
                    else -> Unit
                }
            }
        }


    }

    private fun showLoading() {
        binding.mainCategoryProgressBar.visibility = View.VISIBLE
    }
    private fun hideLoading(){
        binding.mainCategoryProgressBar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }

}