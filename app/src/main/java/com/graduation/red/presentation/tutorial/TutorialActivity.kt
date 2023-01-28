package com.graduation.red.presentation.tutorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.graduation.red.R
import com.graduation.red.base.BaseActivity
import com.graduation.red.databinding.ActivityTutorialBinding
import com.graduation.red.presentation.authentication.AuthenticationActivity
import com.graduation.red.presentation.enableFullScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TutorialActivity : BaseActivity<ActivityTutorialBinding>() , TutorialListener {
    override val layoutRes: Int
        get() = R.layout.activity_tutorial

    private lateinit var slideViewPager: ViewPager2

    override fun initUI(savedInstanceState: Bundle?) {
        binding.listener = this
        enableFullScreen(this)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        initViewPager()
    }

    private fun initViewPager() {
        slideViewPager = binding.tutorialViewPager
        slideViewPager.adapter = TutorialAdapter(this, this)
        slideViewPager.scrollBarFadeDuration = 500
        val dotsIndicator = binding.pageIndicator
        dotsIndicator.attachTo(slideViewPager)

        slideViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                checkLastPage(position)
                if (position == 0 || position == 4) binding.backView.isVisible = false
            }
        })
    }

    private fun checkLastPage(position: Int) {
        if (position == 3) {
            binding.backView.isVisible = false
            binding.nextView.isVisible = false
            binding.btnStart.isVisible = true

        } else {
            binding.backView.isVisible = true
            binding.nextView.isVisible = true
            binding.btnStart.isVisible = false
        }
    }

    private fun getItem(): Int {
        return slideViewPager.currentItem
    }
    override fun clickNext() {
        slideViewPager.setCurrentItem(getItem() + 1, true)
    }

    override fun clickBack() {
        slideViewPager.setCurrentItem(getItem() - 1, true)
    }

    override fun clickStart() {
        val intent = Intent(this , AuthenticationActivity::class.java)
        startActivity(intent)
        finish()
    }
}