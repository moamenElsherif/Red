package com.graduation.red.base.pref


import android.content.Context
import com.google.gson.Gson
import com.graduation.red.base.BasePreferenceStorage
import com.graduation.red.presentation.authentication.createaccount.CreateAccountModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class MyPrefs @Inject constructor(@ApplicationContext context: Context) : BasePreferenceStorage(context) {
    private  val tutorialState = "tutorialState"
    private val userPref = "userPref"

    fun getTutorialState(): String? {
        return getString(tutorialState, "")
    }

    fun setTutorialState(type: String) {
        putString(tutorialState, type)
    }

    fun setUserDetails(type: CreateAccountModel){
        putString(userPref , type.toString())
    }

    fun getUserDetails(): CreateAccountModel {
        return try {
            val authData = getString(userPref, null)
            Gson().fromJson(authData, CreateAccountModel::class.java)
        } catch (ex: Exception) {
            CreateAccountModel()
        }
    }

    fun clearUserPref(){
        putString(userPref , "")
    }
}