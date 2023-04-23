package com.graduation.red.presentation.authentication.createaccount

data class CreateAccountModel(
    var firstName: String = "",
    var lastName: String ="",
    var gender: Int = 1,
    var id: String = "",
    var password: String = "",
    var bloodType: String = ""
)