package com.graduation.red

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.graduation.red.base.BaseActivity
import com.graduation.red.databinding.ActivityMainBinding
import com.graduation.red.presentation.home.donate.DonateFragment
import com.graduation.red.presentation.home.profile.ProfileFragment
import com.graduation.red.presentation.home.request.RequestFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_main

    lateinit var bottomNav : BottomNavigationView


    override fun initUI(savedInstanceState: Bundle?) {
        bottomNav = binding.bottomNav
        handleBottomNav()
        bottomNav.selectedItemId = R.id.donate
    }

    private fun handleBottomNav() {
        loadFragment(DonateFragment())
        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.donate -> {
                    loadFragment(DonateFragment())
                    true
                }
                R.id.request -> {
                    loadFragment(RequestFragment())
                    true
                }
                R.id.settings -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> {
                    loadFragment(DonateFragment())
                    true
                }
            }
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}