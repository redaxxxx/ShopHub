package com.android.developer.prof.reda.shophub.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.adapters.SearchAdapter
import com.android.developer.prof.reda.shophub.data.Product
import com.android.developer.prof.reda.shophub.databinding.FragmentSearchBinding
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "SearchFragment"
@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel by viewModels<SearchViewModel>()
    private var productList = emptyList<Product>()
    private val searchAdapter by lazy { SearchAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            searchViewModel.product.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        Log.d(TAG, "Loading!!!")
                    }
                    is Resource.Success ->{
                        productList = it.data!!


                        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                binding.searchView.clearFocus()
                                if (query != null) {
                                    Log.d(TAG, "query is $query")
                                    searchProduct(query)
                                }else{
                                    Log.d(TAG, "query is null")
                                }
                                return false
                            }

                            override fun onQueryTextChange(newText: String?): Boolean {
                                TODO("Not yet implemented")
                            }

                        })

                    }
                    is Resource.Error ->{
                        Log.d(TAG, it.message.toString())
                    }
                    else -> Unit
                }
            }
        }


    }

    private fun searchProduct(query: String){
        if (query.isNotEmpty()){
            val searchQuery = query.substring(0,1).uppercase() + query.substring(1).lowercase()

            val results = arrayListOf<Product>()
            productList.forEach {product->
                if (product.name != null && product.name.contains(searchQuery))
                    results.add(product)
            }

            searchAdapter.submitList(results)
            binding.rvSearchResult.adapter = searchAdapter
            searchAdapter.notifyDataSetChanged()

        }
    }
}