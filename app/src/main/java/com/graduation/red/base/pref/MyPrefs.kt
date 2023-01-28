package com.graduation.red.base.pref


import android.content.Context
import com.graduation.red.base.BasePreferenceStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class MyPrefs @Inject constructor(@ApplicationContext context: Context) : BasePreferenceStorage(context) {
    private  val tutorialState = "tutorialState"

    fun getTutorialState(): String? {
        return getString(tutorialState, "")
    }

    fun setTutorialState(type: String) {
        putString(tutorialState, type)
    }

    fun clear() {
        remove(tutorialState)
    }
}