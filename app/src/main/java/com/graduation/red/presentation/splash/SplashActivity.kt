package com.graduation.red.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.graduation.red.MainActivity
import com.graduation.red.R
import com.graduation.red.base.BaseActivity
import com.graduation.red.base.pref.MyPrefs
import com.graduation.red.databinding.ActivitySplashBinding
import com.graduation.red.presentation.authentication.AuthenticationActivity
import com.graduation.red.presentation.tutorial.TutorialActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_splash

    @Inject
    lateinit var myPrefs: MyPrefs

    override fun initUI(savedInstanceState: Bundle?) {
        initSplashScreen()
    }

    private fun initSplashScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Handler(Looper.getMainLooper()).postDelayed({
                if (myPrefs.getUserDetails().id.isNotEmpty()) openMain()
                else openAuth()
        }, 3000)
    }

    private fun showTutorial() {
        val intent = Intent(this, TutorialActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openAuth() {
        val intent = Intent(this, AuthenticationActivity::class.java)
        startActivity(intent)
        finish()
    }
}
