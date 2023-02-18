package com.graduation.red.presentation.home.request

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.graduation.red.R
import com.graduation.red.base.BaseFragment
import com.graduation.red.databinding.FragmentRequestBinding

class RequestFragment :BaseFragment<FragmentRequestBinding>(){
    override val layoutRes: Int
        get() = R.layout.fragment_request

    override fun initUI(savedInstanceState: Bundle?) {
    }

}