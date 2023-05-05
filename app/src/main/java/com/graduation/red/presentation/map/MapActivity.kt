package com.graduation.red.presentation.map

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.graduation.red.R
import com.graduation.red.base.BaseActivity
import com.graduation.red.base.pref.MyPrefs
import com.graduation.red.databinding.ActivityMapBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MapActivity : BaseActivity<ActivityMapBinding>(), OnMapReadyCallback {
    override val layoutRes: Int
        get() = R.layout.activity_map

    private lateinit var mMap: GoogleMap
    @Inject
    lateinit var myPrefs: MyPrefs
    private var selectedLocation: LatLng? = null

    override fun initUI(savedInstanceState: Bundle?) {
        selectedLocation = myPrefs.getUserLocation()
        val mapView = supportFragmentManager.findFragmentById(R.id.map_view) as? SupportMapFragment
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
        handleSubmitClick()
    }

    private fun handleSubmitClick() {
        binding.btnSubmitLocation.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("result", selectedLocation?.let { it1 -> toGsonString(it1) })
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.addMarker(
            MarkerOptions()
                .position(selectedLocation!!)
                .title("current location")
        )
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                selectedLocation!!, 15f
            )
        )
        handleClick()
    }

    private fun handleClick() {
        mMap.setOnMapClickListener { latLng ->
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(latLng))
            selectedLocation = latLng
        }
    }
}