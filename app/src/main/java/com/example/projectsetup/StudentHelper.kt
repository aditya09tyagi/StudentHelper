package com.example.projectsetup

import android.app.Activity
import android.app.Application
import android.app.Service
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.projectsetup.data.event.NoInternetEvent
import com.example.projectsetup.di.components.DaggerStudentHelperApplicationComponent
import com.example.projectsetup.di.components.StudentHelperApplicationComponent
import com.example.projectsetup.di.modules.helper.ContextModule
import com.jakewharton.threetenabp.AndroidThreeTen
import es.dmoral.toasty.Toasty
import io.github.inflationx.viewpump.ViewPump
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

class StudentHelper :Application(), LifecycleObserver {

    private lateinit var component:StudentHelperApplicationComponent

    private lateinit var toast: Toast

    companion object {
        const val defaultOffset = 0
        var isAppInForeground = false

        fun get(activity: Activity): StudentHelper {
            return activity.application as StudentHelper
        }

        fun get(service: Service): StudentHelper {
            return service.application as StudentHelper
        }
    }

    override fun onCreate() {
        super.onCreate()

        addProcessObserver()
        initComponent()
        initAndroidThreeTen()
        initTimber()
        initViewPump()
        listenNoInternetEvent()    }

    private fun addProcessObserver() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun initAndroidThreeTen() {
        AndroidThreeTen.init(this)
    }

    private fun initTimber() {
        Timber.plant(component.timberTree())
    }

    private fun initViewPump() {
        ViewPump.init(ViewPump.builder()
            .addInterceptor(component.calligraphyInterceptor())
            .build())
    }

    private fun initComponent() {
        component = DaggerStudentHelperApplicationComponent.builder()
            .contextModule(ContextModule(this))
            .build()
    }

    private fun listenNoInternetEvent() {
        EventBus.getDefault().register(this)
    }

    fun studentHelperApplicationComponent(): StudentHelperApplicationComponent{
        return component
    }

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppGoBackground() {
        isAppInForeground = false
    }

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppComeForeground() {
        isAppInForeground = true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNoInternetEvent(noInternetEvent: NoInternetEvent) {
        showToast()
    }

    private fun showToast() {
        if(!::toast.isInitialized) {
            toast = Toasty.error(this, getString(R.string.no_internet), Toasty.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, defaultOffset, defaultOffset)
        }
        toast.show()
    }
}