package com.example.projectsetup.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import com.example.projectsetup.R
import kotlinx.android.synthetic.main.dialog_update_progress.*

class UpdateProgressDialog(
    context: Context
) : Dialog(context) {

    private lateinit var onSubmitClickListener: OnSubmitClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_update_progress)
        window?.let {
            it.setDimAmount(0.55f)
            it.setGravity(Gravity.CENTER)
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.setBackgroundDrawableResource(android.R.color.white)
        }
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        setListener()
    }

    private fun setListener() {
        btnSubmit.setOnClickListener {
            val progressValue = etUpdateProgressValue.text.toString()
            if (::onSubmitClickListener.isInitialized)
                onSubmitClickListener.onSubmitClick(progressValue.toInt())
        }

        ivClose.setOnClickListener {
            dismiss()
        }
    }

    interface OnSubmitClickListener {
        fun onSubmitClick(updatedProgressValue: Int)
    }

    fun setOnSubmitClickListener(onSubmitClickListener: OnSubmitClickListener) {
        this.onSubmitClickListener = onSubmitClickListener
    }
}