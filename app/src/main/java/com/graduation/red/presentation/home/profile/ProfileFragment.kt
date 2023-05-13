package com.graduation.red.presentation.home.profile

import android.content.Intent
import android.os.Bundle
import com.graduation.red.R
import com.graduation.red.base.BaseFragment
import com.graduation.red.base.pref.MyPrefs
import com.graduation.red.databinding.FragmentProfileBinding
import com.graduation.red.presentation.authentication.AuthenticationActivity
import com.graduation.red.presentation.home.profile.requests_summery.RequestsSummeryActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_profile

    @Inject
    lateinit var myPrefs: MyPrefs

    override fun initUI(savedInstanceState: Bundle?) {
        binding.info = myPrefs.getUserDetails()
        handleClicks()

    }

    private fun handleClicks() {
        binding.btnLogout.setOnClickListener {
            LogoutCheckDialog().showDialog(
                this.requireContext(),
                object : LogoutCheckDialog.LogoutCheckListener {
                    override fun onClickNo() {}
                    override fun onClickYes() {
                        logout()
                    }
                })
        }

        binding.profileView.setOnClickListener {
            openProfileData()
        }

        binding.requestsView.setOnClickListener {
            openRequests()
        }
    }

    private fun openRequests() {
        startActivity(Intent(this.requireActivity(), RequestsSummeryActivity::class.java))
    }

    private fun openProfileData() {

    }

    private fun logout() {
        myPrefs.clearUserPref()
        startActivity(Intent(this.requireContext(), AuthenticationActivity::class.java))
        this.requireActivity().finish()
    }
}
