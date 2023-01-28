package com.graduation.red.base

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

open class BasePreferenceStorage(val context: Context?) {

    protected var pref: SharedPreferences?

    open val PREF = "pref"

    init {
        pref = context?.getSharedPreferences(PREF, MODE_PRIVATE)
    }


    fun putString(key: String, value: String?) {
        val editor = pref?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun putInt(key: String, value: Int) {
        val editor = pref?.edit()
        editor?.putInt(key, value)
        editor?.apply()
    }

    fun getString(key: String, default: String?): String? {
        return pref?.getString(key, default)
    }

    fun getInt(key: String, default: Int): Int {
        return pref?.getInt(key, default) ?: default
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = pref?.edit()
        editor?.putBoolean(key, value)
        editor?.apply()
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return pref?.getBoolean(key, default) == true
    }

    fun remove(key: String) {
        pref?.edit()?.remove(key)?.apply()
    }

}
