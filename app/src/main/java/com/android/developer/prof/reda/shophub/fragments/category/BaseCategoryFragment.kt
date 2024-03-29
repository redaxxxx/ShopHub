package com.android.developer.prof.reda.shophub.fragments.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.OnScrollListener
import androidx.core.widget.NestedScrollView
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.adapters.BestProductAdapter
import com.android.developer.prof.reda.shophub.databinding.FragmentBaseCategoryBinding
import com.android.developer.prof.reda.shophub.fragments.home.HomeFragmentDirections
import com.android.developer.prof.reda.shophub.util.showBottomNavigationView

open class BaseCategoryFragment : Fragment() {

    private lateinit var binding: FragmentBaseCategoryBinding
    protected lateinit var productAdapter: BestProductAdapter
    protected lateinit var offerAdapter: BestProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_base_category,
            container,
            false
        )

        binding.rvOffer.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && dx != 0){
                    onOfferPagingRequest()
                }
            }
        })

        binding.nestedScrollBaseCategory.setOnScrollChangeListener(OnScrollChangeListener { v,_,scrollY,_,_ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY){
                onBestProductsPagingRequest()
            }
        })
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productAdapter = BestProductAdapter(BestProductAdapter.OnItemCLickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment2(it))
        })

        offerAdapter = BestProductAdapter(BestProductAdapter.OnItemCLickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment2(it))
        })

        binding.rvOffer.adapter = offerAdapter
        binding.rvBestProductsBaseCategory.adapter = productAdapter


    }
    fun onBestProductsPagingRequest() {

    }

    fun onOfferPagingRequest() {

    }

    fun showOfferProductLoading(){
        binding.offerProductsProgressBar.visibility = View.VISIBLE
    }
    fun hideOfferProductLoading(){
        binding.offerProductsProgressBar.visibility = View.GONE
    }
    fun showBestProductLoading(){
        binding.bestProductsProgressBar.visibility = View.VISIBLE
    }
    fun hideBestProductLoading(){
        binding.bestProductsProgressBar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }
}