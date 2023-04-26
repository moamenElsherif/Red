package com.graduation.red.presentation.authentication.createaccount

import com.google.gson.Gson

data class CreateAccountModel(
    var firstName: String = "",
    var lastName: String ="",
    var gender: Int = 1,
    var id: String = "",
    var password: String = "",
    var bloodType: String = ""
){

    override fun toString(): String {
        return Gson().toJson(this)
    }
}