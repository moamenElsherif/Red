package com.graduation.red.presentation.home.donate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.graduation.red.base.DiffCallback
import com.graduation.red.databinding.DonateItemBinding
import com.graduation.red.presentation.home.request.RequestsModel

class DonateAdapter(private val donateListener: DonateListener) :
    ListAdapter<RequestsModel, DonateAdapter.CartItemViewHolder>(DiffCallback<RequestsModel>()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        return CartItemViewHolder.form(parent)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val requestItem = getItem(position)
        holder.bind(requestItem , donateListener)
    }

    class CartItemViewHolder(private val binding: DonateItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(requestModel: RequestsModel , donateListener: DonateListener) {
            binding.apply {
                item = requestModel
                listener = donateListener
                executePendingBindings()
            }
        }


        companion object {
            fun form(parent: ViewGroup): CartItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DonateItemBinding.inflate(layoutInflater, parent, false)
                return CartItemViewHolder(binding)
            }
        }
    }
}