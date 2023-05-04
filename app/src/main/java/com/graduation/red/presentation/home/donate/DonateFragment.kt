package com.graduation.red.presentation.home.donate

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.graduation.red.R
import com.graduation.red.base.BaseFragment
import com.graduation.red.base.pref.MyPrefs
import com.graduation.red.databinding.FragmentDonateBinding
import com.graduation.red.presentation.Constants
import com.graduation.red.presentation.Constants.Companion.RequestDocument
import com.graduation.red.presentation.home.request.RequestsModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DonateFragment : BaseFragment<FragmentDonateBinding>(), DonateListener {
    override val layoutRes: Int
        get() = R.layout.fragment_donate

    private val donateAdapter: DonateAdapter = DonateAdapter(this)
    private val db = Firebase.firestore

    @Inject
    lateinit var myPrefs: MyPrefs

    var verifiedRequestByUserCount = 0

    override fun initUI(savedInstanceState: Bundle?) {
        getRequestsList()
    }

    private fun getRequestsList() {
        showLoading()
        db.collection(RequestDocument).get().addOnSuccessListener {
            hideLoading()
            val list = it.toObjects<RequestsModel>()
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
                val list = getRequestListWithoutVerifiedRequests(result)
                donateAdapter.submitList(list)
                adapter = donateAdapter
                binding.tvRequestCount.text = list.size.toString()
            }
        }
    }

    private fun getRequestListWithoutVerifiedRequests(result: List<RequestsModel>): List<RequestsModel> {
        val list = mutableListOf<RequestsModel>()
        val userId = myPrefs.getUserDetails().id
        if (result.isNotEmpty()) {
            result.forEach {
                if (!it.verifiedDonorsId.contains(userId))
                    list.add(it)
                else verifiedRequestByUserCount += 1
            }
        }
        return list
    }

    override fun clickDetails(requestId: String) {

    }

    override fun clickDonate(requestId: String) {
        DonateCheckDialog().showDialog(
            this.requireContext(),
            object : DonateCheckDialog.DonateCheckListener {
                override fun onClickNo() {

                }

                override fun onClickYes() {
                    submitUserDonation(requestId)
                }
            })
    }

    private fun submitUserDonation(requestId: String) {
        val newElement = myPrefs.getUserDetails().id
        val document = db.collection(Constants.RequestDocument).document(requestId)
        document.update("verifiedDonorsId", FieldValue.arrayUnion(newElement))
            .addOnSuccessListener {
                getRequestsList()
            }.addOnFailureListener { e ->
                createToast(e.toString())
            }
    }
}