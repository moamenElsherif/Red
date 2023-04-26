package com.graduation.red.presentation.home.request

import com.graduation.red.R
import com.graduation.red.presentation.enums.ForWhoEnum
import com.graduation.red.presentation.enums.RequestStatusEnum

data class RequestsModel(
    val userId: String = "",
    val status: Int = RequestStatusEnum.NEW.value,
    var forWho: Int = ForWhoEnum.FOR_ME.value,
    val firstName: String = "",
    val lastName: String = "",
    val date: String = "",
    var requestedBloodType: String = "A+",
    val verifiedPhoneNumber:String = "",
    val donationAddress: String = "",
    val messageToDonor: String = "",
    val verifiedDonorsId: List<String> = listOf(),
    var gender: Int = 1
) {

//    fun getForWho():String{
//        return if (forWho==ForWhoEnum.FOR_ME.value) "For Himself"
//        else "For Other"
//    }
}