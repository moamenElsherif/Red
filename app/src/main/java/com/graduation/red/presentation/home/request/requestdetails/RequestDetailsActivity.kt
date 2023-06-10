package com.graduation.red.presentation.home.request.requestdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RequestDetailsActivity : BaseActivity<ActivityRequestDetailsBinding>(),
    RequestDetailsListener {
    override val layoutRes: Int
        get() = R.layout.activity_request_details

    lateinit var requestModel: RequestsModel

    private val db = Firebase.firestore

    @Inject
    lateinit var myPrefs: MyPrefs

    override fun initUI(savedInstanceState: Bundle?) {
        requestModel = intent.extras?.getParcelable(REQUEST_MODEL) ?: RequestsModel()
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

    override fun navigateLocation(lat: String, lng: String) {
        val uri = Uri.parse("google.navigation:q=$lat,$lng")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun submitUserDonation() {
        showLoading()
        val newElement = myPrefs.getUserDetails().id
        val document = db.collection(Constants.RequestDocument).document(requestModel.requestId)
        document.update("verifiedDonorsId", FieldValue.arrayUnion(newElement))
            .addOnSuccessListener {
                hideLoading()
                addToUserDonationList(requestModel.requestId)
                onBack()
            }.addOnFailureListener { e ->
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun addToUserDonationList(requestId: String) {
        lifecycleScope.launch {
            val userDocument = db.collection(Constants.UsersDocument).document(myPrefs.getUserDetails().id)
            userDocument.update("submittedDonateList" , FieldValue.arrayUnion(requestId))
                .addOnFailureListener {
                    Toast.makeText(this@RequestDetailsActivity, "unknown error", Toast.LENGTH_SHORT).show()
                }
        }
    }

}