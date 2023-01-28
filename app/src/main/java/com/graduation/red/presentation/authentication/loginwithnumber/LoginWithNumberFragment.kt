package com.graduation.red.presentation.authentication.loginwithnumber

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.graduation.red.R
import com.graduation.red.base.BaseFragment
import com.graduation.red.databinding.FragmentLoginWithNumberBinding

class LoginWithNumberFragment : BaseFragment<FragmentLoginWithNumberBinding>()  , LoginWithNumberListener{
    override val layoutRes: Int
        get() = R.layout.fragment_login_with_number

    override fun initUI(savedInstanceState: Bundle?) {
        binding.listener = this
    }

    override fun onClickSubmit() {
        findNavController().navigate(LoginWithNumberFragmentDirections.actionLoginWithNumberFragmentToVerifyNumberFragment())
//        findNavController().navigate(LoginWithNumberFragmentDirections.actionLoginWithNumberFragmentToLoginWithPasswordFragment(
//            binding.tvPhoneNumber.text.toString()
//        ))
    }

}