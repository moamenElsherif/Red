package com.graduation.red

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.graduation.red.base.BaseActivity
import com.graduation.red.base.pref.MyPrefs
import com.graduation.red.databinding.ActivityMainBinding
import com.graduation.red.presentation.home.donate.DonateFragment
import com.graduation.red.presentation.home.profile.ProfileFragment
import com.graduation.red.presentation.home.request.RequestFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_main

    private lateinit var bottomNav : BottomNavigationView
    @Inject
    lateinit var myPrefs: MyPrefs
    private var currentLocation: LatLng? = null


    override fun initUI(savedInstanceState: Bundle?) {
        bottomNav = binding.bottomNav
        handleBottomNav()
        bottomNav.selectedItemId = R.id.donate
        handleLocation()
    }

    private fun handleLocation() {
        val locationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            locationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    currentLocation = LatLng(location.latitude, location.longitude)
                    myPrefs.setUserLocation(currentLocation!!)
                }
            }
        }
    }

    private fun handleBottomNav() {
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

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }
}