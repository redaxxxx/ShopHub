package com.android.developer.prof.reda.shophub.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.developer.prof.reda.shophub.R
/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteProductsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteProductsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_products, container, false)
    }

}