package com.graduation.red.presentation.authentication.loginwithpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.graduation.red.R
import com.graduation.red.base.BaseFragment
import com.graduation.red.databinding.FragmentLoginWithPasswordBinding


class LoginWithPasswordFragment : BaseFragment<FragmentLoginWithPasswordBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_login_with_password

    private val args: LoginWithPasswordFragmentArgs by navArgs()

    override fun initUI(savedInstanceState: Bundle?) {
        binding.tvPhoneNumber.text = args.phone.toString()
    }

}