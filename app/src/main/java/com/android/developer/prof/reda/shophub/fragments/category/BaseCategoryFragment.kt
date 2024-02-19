package com.android.developer.prof.reda.shophub.fragments.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.adapters.BestProductAdapter
import com.android.developer.prof.reda.shophub.databinding.FragmentBaseCategoryBinding

open class BaseCategoryFragment : Fragment() {

    private lateinit var binding: FragmentBaseCategoryBinding
    protected val productAdapter: BestProductAdapter by lazy { BestProductAdapter() }
    protected val offerAdapter: BestProductAdapter by lazy { BestProductAdapter() }

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvOffer.adapter = offerAdapter
        binding.rvBestProductsBaseCategory.adapter = productAdapter


    }

}