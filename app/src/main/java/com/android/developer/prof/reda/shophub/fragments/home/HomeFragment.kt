package com.android.developer.prof.reda.shophub.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.adapters.HomeViewPagerAdapter
import com.android.developer.prof.reda.shophub.databinding.FragmentHomeBinding
import com.android.developer.prof.reda.shophub.fragments.category.AccessoryFragment
import com.android.developer.prof.reda.shophub.fragments.category.ChairFragment
import com.android.developer.prof.reda.shophub.fragments.category.ClothesFragment
import com.android.developer.prof.reda.shophub.fragments.category.CupboardFragment
import com.android.developer.prof.reda.shophub.fragments.category.FurnitureFragment
import com.android.developer.prof.reda.shophub.fragments.category.MainCategoryFragment
import com.android.developer.prof.reda.shophub.fragments.category.ShoesFragment
import com.android.developer.prof.reda.shophub.fragments.category.TableFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment(),
            ClothesFragment(),
            ShoesFragment()
        )

        binding.viewPagerHome.isUserInputEnabled = false

        val viewPagerAdapter = HomeViewPagerAdapter(categoriesFragments, childFragmentManager, lifecycle)
        binding.viewPagerHome.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPagerHome){ tab, position->
            when(position){
                0-> tab.text = "Home"
                1-> tab.text = "Chair"
                2-> tab.text = "Cupboard"
                3-> tab.text = "Table"
                4-> tab.text = "Accessory"
                5-> tab.text = "Furniture"
                6-> tab.text = "Clothes"
                7-> tab.text = "Shoes"
            }
        }.attach()
    }

}