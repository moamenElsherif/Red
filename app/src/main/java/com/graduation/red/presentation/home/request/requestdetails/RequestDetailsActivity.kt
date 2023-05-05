package com.graduation.red.presentation.home.request.requestdetails

import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.graduation.red.R
import com.graduation.red.base.BaseActivity
import com.graduation.red.base.pref.MyPrefs
import com.graduation.red.databinding.ActivityRequestDetailsBinding
import com.graduation.red.presentation.Constants
import com.graduation.red.presentation.Constants.Companion.REQUEST_MODEL
import com.graduation.red.presentation.home.request.RequestsModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RequestDetailsActivity : BaseActivity<ActivityRequestDetailsBinding>(), RequestDetailsListener {
    override val layoutRes: Int
        get() = R.layout.activity_request_details

    lateinit var requestModel: RequestsModel

    private val db = Firebase.firestore

    @Inject
    lateinit var myPrefs: MyPrefs

    override fun initUI(savedInstanceState: Bundle?) {
        requestModel = intent.extras?.getParcelable(REQUEST_MODEL)?: RequestsModel()
        initData()
    }

    private fun initData() {
        binding.item = requestModel
        binding.listener = this
    }

    override fun onBack() {
        this.finish()
    }

    override fun onSubmit() {
        submitUserDonation()
    }

    override fun showLocation() {

    }

    private fun submitUserDonation() {
        showLoading()
        val newElement = myPrefs.getUserDetails().id
        val document = db.collection(Constants.RequestDocument).document(requestModel.requestId)
        document.update("verifiedDonorsId", FieldValue.arrayUnion(newElement))
            .addOnSuccessListener {
                hideLoading()
                onBack()
            }.addOnFailureListener { e ->
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
    }

}