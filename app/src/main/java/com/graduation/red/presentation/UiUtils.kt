package com.graduation.red.presentation

import android.view.View
import android.view.WindowManager

fun enableFullScreen(activity: androidx.fragment.app.FragmentActivity?) {
    //show system UI
    activity!!.window?.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )

    //show navigation bar
    activity.window!!.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY


}