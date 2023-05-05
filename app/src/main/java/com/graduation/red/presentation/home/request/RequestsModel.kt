package com.graduation.red.presentation.home.request

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.graduation.red.presentation.enums.ForWhoEnum
import com.graduation.red.presentation.enums.RequestStatusEnum
import kotlinx.parcelize.Parcelize

@Parcelize
data class RequestsModel(
    val requestId: String = "",
    val userId: String = "",
    val status: Int = RequestStatusEnum.NEW.value,
    var forWho: Int = ForWhoEnum.FOR_ME.value,
    val firstName: String = "",
    val lastName: String = "",
    val date: String = "",
    var requestedBloodType: String = "A+",
    val verifiedPhoneNumber: String = "",
    val donationAddress: String = "",
    val messageToDonor: String = "",
    val verifiedDonorsId: List<String> = listOf(),
    var gender: Int = 1,
    var locationLat : Double? = null,
    var locationLng : Double? = null,
    var locationAddress: String = ""
):Parcelable {

    fun getForWhoString(): String {
        return if (forWho == ForWhoEnum.FOR_ME.value) "For Himself"
        else "For Other"
    }
}