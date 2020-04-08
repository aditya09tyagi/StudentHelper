package com.example.projectsetup.ui.notification

import android.content.Context
import android.content.Intent
import com.example.projectsetup.ui.splash.SplashActivity
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal


class NotificationOpenedHandler(
    val context: Context
) : OneSignal.NotificationOpenedHandler {

    private var notificationType: Int? = null

    override fun notificationOpened(result: OSNotificationOpenResult?) {

        result?.notification?.payload?.additionalData?.let {
            val intent = SplashActivity.newIntent(context)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }


}