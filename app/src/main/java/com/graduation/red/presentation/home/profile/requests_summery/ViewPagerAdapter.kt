package com.graduation.red.presentation.home.profile.requests_summery

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ActiveRequestsFragment()
            1 -> ExpiredRequestsFragment()
            else -> ActiveRequestsFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Active"
            1 -> "Expired"
            else -> null
        }
    }

}