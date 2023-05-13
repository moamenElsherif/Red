package com.graduation.red.presentation.home.profile.requests_summery

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.graduation.red.R
import com.graduation.red.base.BaseActivity
import com.graduation.red.databinding.ActivityRequestsSummeryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestsSummeryActivity : BaseActivity<ActivityRequestsSummeryBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_requests_summery

    override fun initUI(savedInstanceState: Bundle?) {
        handleViewPager()
    }

    private fun handleViewPager() {
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        tabLayout.setTabTextColors(
            ContextCompat.getColor(this, R.color.black),
            ContextCompat.getColor(this, R.color.bright_red))
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }
}