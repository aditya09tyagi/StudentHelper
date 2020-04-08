package com.example.projectsetup.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import com.example.projectsetup.R
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.dialog_notification_sender.*

class NotificationSenderDialog(
    context: Context
) : Dialog(context) {

    private lateinit var onSubmitNotificationClickListener: OnSubmitNotificationClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_notification_sender)
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
        btnSendNotification.setOnClickListener {
            val notificationTitle = etNotificationTitle.text.toString()
            val notificationDescription = etNotificationDesc.text.toString()
            if (::onSubmitNotificationClickListener.isInitialized) {
                if (notificationDescription.isNotEmpty() && notificationTitle.isNotEmpty())
                    onSubmitNotificationClickListener.onSubmitNotificationClick(notificationTitle, notificationDescription)
                else
                    Toasty.error(context, "Title and description cannot be left empty.").show()
            }
        }

        ivClose.setOnClickListener {
            dismiss()
        }
    }

    interface OnSubmitNotificationClickListener {
        fun onSubmitNotificationClick(notificationTitle: String, notificationDescription: String)
    }

    fun setOnSubmitNotificationClickListener(onSubmitNotificationClickListener: OnSubmitNotificationClickListener) {
        this.onSubmitNotificationClickListener = onSubmitNotificationClickListener
    }
}