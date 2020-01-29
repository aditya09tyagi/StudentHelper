package com.example.projectsetup.ui.loader

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.projectsetup.R

class SearchModal(context: Context, val message: String? = null) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_searching)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setDimAmount(0.95f)
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }
}