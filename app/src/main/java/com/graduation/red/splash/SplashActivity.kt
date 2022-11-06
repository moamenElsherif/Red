package com.graduation.red.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import com.graduation.red.MainActivity
import com.graduation.red.R
import com.graduation.red.base.BaseActivity
import com.graduation.red.databinding.ActivitySplashBinding


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_splash

    override fun initUI(savedInstanceState: Bundle?) {
        initSplashScreen()
    }

    private fun initSplashScreen(){
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

//    private fun showFadingText() {
//        Handler(Looper.getMainLooper()).postDelayed({
//            binding.textView2.visibility = View.VISIBLE
//        }, 2000)
//

//        var endAction: Runnable?
//        binding.textView2.animate().alpha(1f).setDuration(1000).setStartDelay(0).withEndAction(
//            Runnable { binding.textView2.animate().alpha(0f).setDuration(1000).startDelay = 2000 }.also {
//                endAction = it
//            }
//        )
    }
