package com.graduation.red.presentation

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import com.graduation.red.R

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

fun showLoadingDialog(activity: Activity?, hint: String?): Dialog? {
    if (activity == null || activity.isFinishing) {
        return null
    }
    val progressDialog = Dialog(activity)
    progressDialog.show()
    if (progressDialog.window != null) {
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
    progressDialog.setContentView(R.layout.progress_dialog)
    progressDialog.setCancelable(false)
    progressDialog.setCanceledOnTouchOutside(false)
    progressDialog.show()

    return progressDialog
}

fun hideLoadingDialog(mProgressDialog: Dialog?, activity: Activity?) {
    if (activity != null && !activity.isFinishing && mProgressDialog != null && mProgressDialog.isShowing) {
        mProgressDialog.dismiss()
    }
}