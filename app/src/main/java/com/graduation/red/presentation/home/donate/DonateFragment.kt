package com.graduation.red.presentation.home.donate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.graduation.red.R
import com.graduation.red.base.BaseFragment
import com.graduation.red.databinding.FragmentDonateBinding
import com.graduation.red.presentation.Constants.Companion.RequestDocument
import com.graduation.red.presentation.authentication.createaccount.CreateAccountModel
import com.graduation.red.presentation.home.request.RequestsModel


class DonateFragment : BaseFragment<FragmentDonateBinding>() , DonateListener {
    override val layoutRes: Int
        get() = R.layout.fragment_donate

    private val donateAdapter: DonateAdapter = DonateAdapter(this)
    private val db = Firebase.firestore


    override fun initUI(savedInstanceState: Bundle?) {
        getRequestsList()
    }

    private fun getRequestsList() {
        showLoading()
        db.collection(RequestDocument).get().addOnSuccessListener {
            hideLoading()
            val list = it.toObjects<RequestsModel>()
            binding.tvRequestCount.text = list.size.toString()
            initAdapter(list)
        }.addOnFailureListener {
            hideLoading()
            createToast(it.toString())
        }
    }


    private fun initAdapter(result: List<RequestsModel>) {
        binding.apply {
            rvRequest.apply {
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    this.isSmoothScrolling
                }
                setHasFixedSize(false)
                donateAdapter.submitList(result)
                adapter = donateAdapter
            }
        }
    }
    override fun clickDetails() {

    }

    override fun clickDonate() {

    }
}