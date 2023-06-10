package com.graduation.red.presentation.home.request.requestdetails

interface RequestDetailsListener {
    fun onBack()
    fun onSubmit()
    fun navigateLocation(lat: String ,lng: String)
}