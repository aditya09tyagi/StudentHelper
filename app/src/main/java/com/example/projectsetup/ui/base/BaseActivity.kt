package com.example.projectsetup.ui.base

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.projectsetup.R
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.di.components.DaggerBaseActivityComponent
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.example.projectsetup.ui.base.AppCloseEventWatcher.OnHomePressedListener
import com.example.projectsetup.util.SharedPreferenceUtil
import com.jaeger.library.StatusBarUtil
import es.dmoral.toasty.Toasty
import javax.inject.Inject

open class BaseActivity : AppCompatActivity() {

    private lateinit var appCloseEventWatcher: AppCloseEventWatcher

    @Inject
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil

    @Inject
    lateinit var gson: Gson

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        window.navigationBarColor = ContextCompat.getColor(this, R.color.navigation_bar)

        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        StatusBarUtil.setColorNoTranslucent(
            this,
            ContextCompat.getColor(this, R.color.colorPrimaryDark)
        )

        val component = DaggerBaseActivityComponent.builder()
            .studentHelperApplicationComponent(StudentHelper.get(this).studentHelperApplicationComponent())
            .build()

        component.injectBaseActivity(this)

        listenToAppCloseEvent()
    }

    private fun listenToAppCloseEvent() {
        appCloseEventWatcher = AppCloseEventWatcher(applicationContext)

        appCloseEventWatcher.setOnHomePressedListener(object : OnHomePressedListener {
            override fun onHomePressed() {
            }

            override fun onHomeLongPressed() {
            }
        })

        appCloseEventWatcher.startWatch()
    }

    override fun onDestroy() {
        if (::appCloseEventWatcher.isInitialized)
            appCloseEventWatcher.stopWatch()

        super.onDestroy()
    }

    protected fun showErrorToast(
        message: String,
        view: View? = null,
        actionText: String = getString(R.string.ok),
        duration: Int = Toasty.LENGTH_LONG
    ) {

        if (NotificationManagerCompat.from(this).areNotificationsEnabled())
            Toasty.error(this, message, duration).show()
        else if (view != null) {
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snackbar.setAction(actionText) {
                snackbar.dismiss()
            }
            snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.text_red))
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.white))
            snackbar.show()
        }

    }

    protected fun showSuccessToast(
        message: String,
        view: View? = null,
        duration: Int = Toasty.LENGTH_LONG
    ) {
        if (NotificationManagerCompat.from(this).areNotificationsEnabled())
            Toasty.success(this, message, duration).show()
        else if (view != null) {
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snackbar.setAction(getString(R.string.ok)) {
                snackbar.dismiss()
            }
            snackbar.view.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.feedback_positive
                )
            )
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.white))
            snackbar.show()
        }
    }

    protected fun showSuccessSnackBar(
        message: String,
        view: View
    ) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackbar.setAction(getString(R.string.ok)) {
            snackbar.dismiss()
        }
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.feedback_positive))
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.show()
    }

    protected fun showErrorSnackBar(
        message: String,
        view: View,
        actionText: String = getString(R.string.ok)
    ) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackbar.setAction(actionText) {
            snackbar.dismiss()
        }
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.text_red))
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.show()
    }
}