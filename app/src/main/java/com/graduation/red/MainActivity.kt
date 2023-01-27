package com.graduation.red

import android.os.Bundle
import com.graduation.red.base.BaseActivity
import com.graduation.red.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun initUI(savedInstanceState: Bundle?) {
        binding.tvHello.text = "run successfully"
    }

}