package com.graduation.red

import android.content.Intent
import android.os.Bundle
import com.graduation.red.base.BaseActivity
import com.graduation.red.databinding.ActivityMainBinding
import com.graduation.red.presentation.tutorial.TutorialActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun initUI(savedInstanceState: Bundle?) {
        val intent = Intent(this , TutorialActivity::class.java)
        startActivity(intent)
    }

}