package com.android.developer.prof.reda.shophub.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.shophub.R
import com.android.developer.prof.reda.shophub.data.Address
import com.android.developer.prof.reda.shophub.databinding.AddressItemBinding

class AddressAdapter : ListAdapter<Address, AddressAdapter.AddressViewHolder>(DiffCallback) {

    inner class AddressViewHolder(private val binding: AddressItemBinding): ViewHolder(binding.root){
        fun bind(address: Address){
            binding.tvNameOfChooseAddress.text = "${address.firstName} ${address.familyName}"
            binding.tvAddressOfChooseAddress.text = "${address.address}, ${address.city}, ${address.state}, ${address.postalCode}"
            binding.tvPhoneNumberChooseAddress.text = address.phoneNumber

            binding.editAddressBtn.setOnClickListener {
                onEditClick?.invoke(address)
            }

            binding.deleteAddressBtn.setOnClickListener {
                onDeleteClick?.invoke(address)
            }
        }
        fun setStrokeColor(){
            binding.cardViewAddress.strokeColor = binding.root.resources.getColor(R.color.g_blue)
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Address>(){
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.address == newItem.address
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(AddressItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = getItem(position)
        holder.itemView.setOnClickListener{
            onAddressClick?.invoke(address)
            holder.setStrokeColor()
        }
        holder.bind(address)
    }
    var onAddressClick: ((Address) -> Unit) ?= null
    var onEditClick: ((Address) -> Unit) ?= null
    var onDeleteClick: ((Address) -> Unit) ?= null
}