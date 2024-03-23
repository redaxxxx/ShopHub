package com.android.developer.prof.reda.shophub.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.developer.prof.reda.shophub.data.Address
import com.android.developer.prof.reda.shophub.databinding.AddressItemBinding

class AddressAdapter(val addressClickListener: OnClickAddressListener)
    : ListAdapter<Address, AddressAdapter.AddressViewHolder>(DiffCallback) {

    inner class AddressViewHolder(private val binding: AddressItemBinding): ViewHolder(binding.root){
        fun bind(address: Address){
            binding.tvNameOfChooseAddress.text = "${address.firstName} ${address.familyName}"
            binding.tvAddressOfChooseAddress.text = address.address
            binding.tvStateOfChooseAddress.text = address.state
            binding.tvCityOfChooseAddress.text = address.city
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Address>(){
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
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
        holder.bind(address)

        holder.itemView.setOnClickListener{
            addressClickListener.clickListener(address)
        }
    }

    class OnClickAddressListener(val clickListener: (address:Address) -> Unit){
        fun onCLickAddress(address: Address) = clickListener(address)
    }
}