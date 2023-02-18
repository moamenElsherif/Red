package com.graduation.red.presentation.home.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.graduation.red.R
import com.graduation.red.base.BaseFragment
import com.graduation.red.databinding.FragmentProfileBinding


class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_profile

    override fun initUI(savedInstanceState: Bundle?) {
    }
}
