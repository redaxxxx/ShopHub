package com.android.developer.prof.reda.shophub.util

import android.opengl.Visibility
import android.view.View
import androidx.fragment.app.Fragment
import com.android.developer.prof.reda.shophub.activities.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigationView(){
    val bottomNavigationView =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(
            com.android.developer.prof.reda.shophub.R.id.bottomNavView
        )
    bottomNavigationView.visibility = View.GONE
}

fun Fragment.showBottomNavigationView(){
    val bottomNavigationView =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(
            com.android.developer.prof.reda.shophub.R.id.bottomNavView
        )
    bottomNavigationView.visibility = View.VISIBLE
}