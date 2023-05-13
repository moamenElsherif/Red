package com.graduation.red.presentation.home.donate

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.graduation.red.presentation.Constants.Companion.REQUEST_MODEL
import com.graduation.red.presentation.Constants.Companion.RequestDocument
import com.graduation.red.presentation.home.request.RequestFragment
import com.graduation.red.presentation.home.request.RequestsModel
import com.graduation.red.presentation.home.request.requestdetails.RequestDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DonateFragment : BaseFragment<FragmentDonateBinding>(), DonateListener {
    override val layoutRes: Int
        get() = R.layout.fragment_donate

    private val donateAdapter: DonateAdapter = DonateAdapter(this)
    private val db = Firebase.firestore

    private val viewModel: DonateViewModel by viewModels()

    private var list = listOf<RequestsModel>()

    @Inject
    lateinit var myPrefs: MyPrefs

    private var verifiedRequestByUserCount = 0

    override fun initUI(savedInstanceState: Bundle?) {
        initAdapter()
    }

    private fun getRequestsList() {
        showLoading()
        db.collection(RequestDocument).get().addOnSuccessListener {
            hideLoading()
            list = getRequestListWithoutVerifiedRequests(it.toObjects())
            sortList()
        }.addOnFailureListener {
            hideLoading()
            createToast(it.toString())
        }
    }

    private fun sortList() {
        lifecycleScope.launch {
            val lat = myPrefs.getUserLocation().latitude
            val lng = myPrefs.getUserLocation().longitude
            list = viewModel.sortLocationsByDistance(list , lat , lng)
            updateAdapterList()
        }
    }


    private fun initAdapter() {
        binding.apply {
            rvRequest.apply {
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    this.isSmoothScrolling
                }
                setHasFixedSize(false)
                adapter = donateAdapter
            }
        }
    }

    private fun updateAdapterList() {
        donateAdapter.submitList(list)
        binding.tvRequestCount.text = list.size.toString()
        donateAdapter.notifyDataSetChanged()
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

    override fun clickDetails(item: RequestsModel) {
        val intent = Intent(this.requireContext()  , RequestDetailsActivity::class.java)
        intent.putExtra(REQUEST_MODEL , item)
        startActivity(intent)
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
        val document = db.collection(RequestDocument).document(requestId)
        document.update("verifiedDonorsId", FieldValue.arrayUnion(newElement))
            .addOnSuccessListener {
                getRequestsList()
            }.addOnFailureListener { e ->
                createToast(e.toString())
            }
    }

    override fun onResume() {
        super.onResume()
        getRequestsList()
    }
}