package com.graduation.red.presentation.home.profile.requests_summery

import android.content.Intent
import android.os.Bundle
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.graduation.red.R
import com.graduation.red.base.BaseFragment
import com.graduation.red.databinding.FragmentExpiredRequestsBinding
import com.graduation.red.presentation.Constants
import com.graduation.red.presentation.home.request.RequestsModel
import com.graduation.red.presentation.home.request.requestdetails.RequestDetailsActivity

class ExpiredRequestsFragment :BaseFragment<FragmentExpiredRequestsBinding>(), RequestSummeryListener {
    override val layoutRes: Int
        get() = R.layout.fragment_expired_requests

    private val db = Firebase.firestore
    private val donateAdapter: RequestSummeryAdapter = RequestSummeryAdapter(this)

    override fun initUI(savedInstanceState: Bundle?) {

    }

    override fun onItemClick(item: RequestsModel) {
        val intent = Intent(this.requireContext()  , RequestDetailsActivity::class.java)
        intent.putExtra(Constants.REQUEST_MODEL, item)
        startActivity(intent)
    }
}