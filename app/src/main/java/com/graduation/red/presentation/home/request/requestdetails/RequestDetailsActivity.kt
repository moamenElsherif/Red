package com.graduation.red.presentation.home.request.requestdetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.graduation.red.R
import com.graduation.red.base.BaseActivity
import com.graduation.red.databinding.ActivityRequestDetailsBinding
import com.graduation.red.presentation.Constants.Companion.REQUEST_MODEL
import com.graduation.red.presentation.hideLoadingDialog
import com.graduation.red.presentation.home.request.RequestsModel
import com.graduation.red.presentation.showLoadingDialog

class RequestDetailsActivity : BaseActivity<ActivityRequestDetailsBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_request_details

    lateinit var requestModel: RequestsModel

    override fun initUI(savedInstanceState: Bundle?) {
        showLoading()
        requestModel = intent.extras?.getParcelable(REQUEST_MODEL)?: RequestsModel()
        initData()
    }

    private fun initData() {
        binding.item = requestModel
    }

}