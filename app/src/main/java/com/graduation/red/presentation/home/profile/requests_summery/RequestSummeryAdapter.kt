package com.graduation.red.presentation.home.profile.requests_summery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.graduation.red.base.DiffCallback
import com.graduation.red.databinding.RequestStatusItemBinding
import com.graduation.red.presentation.home.request.RequestsModel

class RequestSummeryAdapter(val listener: RequestSummeryListener) :
    ListAdapter<RequestsModel, RequestSummeryAdapter.RequestItemViewHolder>(DiffCallback<RequestsModel>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestItemViewHolder {
        return RequestItemViewHolder.form(parent)
    }

    override fun onBindViewHolder(holder: RequestItemViewHolder, position: Int) {
        val requestItem = getItem(position)
        holder.bind(requestItem, listener)
    }

    class RequestItemViewHolder(private val binding: RequestStatusItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(requestModel: RequestsModel, donateListener: RequestSummeryListener) {
            binding.apply {
                binding.tvStatus.setTextColor(requestModel.getRequestStatusColor())
                item = requestModel
                listener = donateListener
                executePendingBindings()
            }
        }

        companion object {
            fun form(parent: ViewGroup): RequestItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RequestStatusItemBinding.inflate(layoutInflater, parent, false)
                return RequestItemViewHolder(binding)
            }
        }
    }
}