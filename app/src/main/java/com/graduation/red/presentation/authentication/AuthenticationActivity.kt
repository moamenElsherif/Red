package com.graduation.red.presentation.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.graduation.red.R
import com.graduation.red.base.BaseActivity
import com.graduation.red.databinding.ActivityAuthenticationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity : BaseActivity<ActivityAuthenticationBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_authentication

    override fun initUI(savedInstanceState: Bundle?) {
    }

}