package com.example.projectsetup.ui.loader

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.projectsetup.R
import com.github.ybq.android.spinkit.style.DoubleBounce
import kotlinx.android.synthetic.main.layout_progress_modal.*

class ProgressModal(context: Context, val message: String? = null, val dimAmount: Float = 0.95f) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_progress_modal)
        progressBar.indeterminateDrawable = DoubleBounce()
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setDimAmount(dimAmount)
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        message?.let {
            tvMessage.text = it
        }
    }
}