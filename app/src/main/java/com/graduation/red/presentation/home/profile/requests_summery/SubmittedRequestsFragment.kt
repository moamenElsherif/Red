package com.graduation.red.presentation.home.profile.requests_summery

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.graduation.red.R
import com.graduation.red.base.BaseFragment
import com.graduation.red.base.pref.MyPrefs
import com.graduation.red.databinding.FragmentExpiredRequestsBinding
import com.graduation.red.presentation.Constants
import com.graduation.red.presentation.authentication.createaccount.CreateAccountModel
import com.graduation.red.presentation.home.request.RequestsModel
import com.graduation.red.presentation.home.request.requestdetails.RequestDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SubmittedRequestsFragment :BaseFragment<FragmentExpiredRequestsBinding>(), RequestSummeryListener {
    override val layoutRes: Int
        get() = R.layout.fragment_expired_requests

    private val list:  MutableList<RequestsModel> = mutableListOf()
    private val db = Firebase.firestore
    private val requestAdapter: RequestSummeryAdapter = RequestSummeryAdapter(this)
    @Inject
    lateinit var myPrefs: MyPrefs

    override fun initUI(savedInstanceState: Bundle?) {
        initAdapter()
        getMyRequestList()
    }

    private fun initAdapter() {
        binding.apply {
            rvRequests.apply {
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    this.isSmoothScrolling
                }
                setHasFixedSize(false)
                adapter = requestAdapter
            }
        }
    }

    private fun getMyRequestList() {
        showLoading()
        lifecycleScope.launchWhenStarted {
            val document = db.collection(Constants.UsersDocument).document(myPrefs.getUserDetails().id)
            document.get().addOnSuccessListener {
                val result = it.toObject<CreateAccountModel>()
                val requestIdList = result?.submittedDonateList ?: emptyList()
                hideLoading()
                getRequestsList(requestIdList)
            }
        }
    }

    private fun getRequestsList(requestIdList: List<String>) {
        lifecycleScope.launchWhenStarted {
            val document = db.collection(Constants.RequestDocument)
            document.get().addOnSuccessListener {
                val requestModelList = it.toObjects<RequestsModel>()
                requestModelList.forEach {model ->
                    if (requestIdList.contains(model.requestId))
                        list.add(model)
                }
                updateAdapter()
            }
        }
    }

    private fun updateAdapter() {
        requestAdapter.submitList(list)
    }

    override fun onItemClick(item: RequestsModel) {
        val intent = Intent(this.requireContext()  , RequestDetailsActivity::class.java)
        intent.putExtra(Constants.REQUEST_MODEL, item)
        startActivity(intent)
    }
}