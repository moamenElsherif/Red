package com.graduation.red.presentation.home.profile

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import com.graduation.red.R

class LogoutCheckDialog {
    var dialog: Dialog? = null
    private lateinit var mListener: LogoutCheckListener
    private var btnSubmit: Button? = null
    private var btnBack: Button? = null


    fun showDialog(
        context: Context, listener: LogoutCheckListener
    ) {
        val dialog = Dialog(context)
        this.dialog = dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.logout_dialog)
        mListener = listener

        btnBack = dialog.findViewById(R.id.btn_no)
        btnSubmit = dialog.findViewById(R.id.btn_yes)

        btnBack?.setOnClickListener {
            listener.onClickNo()
            dialog.dismiss()
        }

        btnSubmit?.setOnClickListener {
            listener.onClickYes()
            dialog.dismiss()
        }

        val window = dialog.window
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }

    interface LogoutCheckListener {
        fun onClickYes()
        fun onClickNo()
    }
}