package com.android.developer.prof.reda.shophub.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.shophub.databinding.ViewPagerItemBinding
import com.bumptech.glide.Glide

class ViewPagerImagesAdapter: ListAdapter<String, ViewPagerImagesAdapter.ViewPagerImagesViewHolder>(DiffCallback) {

    inner class ViewPagerImagesViewHolder(private val binding: ViewPagerItemBinding): ViewHolder(binding.root){
        fun bind(imageUriString: String){
            Glide.with(itemView)
                .load(imageUriString)
                .into(binding.imageProductDetails)
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerImagesViewHolder {
        return ViewPagerImagesViewHolder(ViewPagerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewPagerImagesViewHolder, position: Int) {
        val imageUrlString = getItem(position)
        holder.bind(imageUrlString)
    }
}