package com.graduation.red.presentation.home.donate

import androidx.lifecycle.ViewModel
import com.graduation.red.presentation.home.request.RequestsModel
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class DonateViewModel : ViewModel(){

    fun sortLocationsByDistance(locations: List<RequestsModel>, myLat: Double, myLng: Double): List<RequestsModel> {
        val sortedLocations = locations.sortedBy { location ->
            val lat = location.locationLat ?: 0.0
            val lng = location.locationLng ?: 0.0
            val distance = haversine(myLat, myLng, lat, lng)
            distance
        }
        return sortedLocations
    }


   private fun haversine(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val radius = 6371 // Earth's radius in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)
        val a =
            sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(
                dLng / 2
            ) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return radius * c
    }
}