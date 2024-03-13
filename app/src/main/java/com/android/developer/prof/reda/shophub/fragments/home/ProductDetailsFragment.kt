package com.android.developer.prof.reda.shophub.fragments.home

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.adapters.ViewPagerImagesAdapter
import com.android.developer.prof.reda.shophub.data.CartProduct
import com.android.developer.prof.reda.shophub.data.Product
import com.android.developer.prof.reda.shophub.databinding.FragmentProductDetailsBinding
import com.android.developer.prof.reda.shophub.util.Resource
import com.android.developer.prof.reda.shophub.util.hideBottomNavigationView
import com.android.developer.prof.reda.shophub.viewmodel.DetailsViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "ProductDetailsFragment"
@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var viewPagerImagesAdapter: ViewPagerImagesAdapter
    private var selectedColor: String ?= null
    private var selectedSize: String ?= null
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigationView()
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_product_details,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productArgs = ProductDetailsFragmentArgs.fromBundle(requireArguments())
        // Setup view pager images
        viewPagerImagesAdapter = ViewPagerImagesAdapter()
        viewPagerImagesAdapter.submitList(productArgs.product.images)
        binding.viewPagerProductImages.adapter = viewPagerImagesAdapter

        binding.apply {
            tvProductName.text = productArgs.product.name
            tvProductPrice.text = productArgs.product.price.toString()
            tvProductDescription.text = productArgs.product.description
        }

        productArgs.product.colors?.let { setupChipGroup(it) }

        binding.addToCartButton.setOnClickListener {
            viewModel.addUpdateProductInCart(CartProduct(productArgs.product, 1, selectedColor, selectedSize))
        }

        lifecycleScope.launch {
            viewModel.addToProduct.collectLatest{
                when(it){
                    is Resource.Loading ->{
                        binding.addToCartButton.startAnimation()
                    }
                    is Resource.Success ->{
                        binding.addToCartButton.revertAnimation()
                    }
                    is Resource.Error ->{
                        binding.addToCartButton.stopAnimation()
                        Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG).show()
                        Log.d(TAG, "Error add product in cart ${it.message.toString()}")
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupChipGroup(color: List<String>){
        val colorsMap = getColorMap(color)
        binding.productColorChipGroup.removeAllViews()
        var ind = 1

        if (colorsMap != null) {
            for (entry: Map.Entry<String,String> in colorsMap){
                val chip = Chip(requireActivity())
                chip.id = ind
                chip.tag = entry.key
                chip.chipStrokeColor = ColorStateList.valueOf(Color.BLACK)
                chip.chipStrokeWidth = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    3F,
                    this.resources.displayMetrics
                )
                chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(entry.value))
                chip.isCheckable = true

                binding.productColorChipGroup.addView(chip)
                ind++
            }
        }
        binding.productColorChipGroup.invalidate()
    }

    private fun getColorMap(colors: List<String>): Map<String, String>{
        val colorMap = mutableMapOf<String, String>()
        for (color in colors){
            when(color){
                "black" -> colorMap["black"] = "#000000"
                "white" ->colorMap["white"] = "#FFFFFF"
                "red" ->colorMap["red"] = "#FF0000"
                "green" ->colorMap["green"] = "#00FF00"
                "blue" -> colorMap["blue"] = "0000FF"
                "yellow" -> colorMap["yellow"] = "#FFFF00"
                "cyan" -> colorMap["cyan"] = "#00FFFF"
                "magenta" -> colorMap["magenta"] = "#FF00FF"
                "gray" -> colorMap["gray"] = "#808080"
                "coffee" -> colorMap["coffee"] = "#6F4E37"
                else -> Unit
            }
        }

        return colorMap
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.addToCartButton.dispose()
    }
}