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
import com.example.projectsetup.di.components.DaggerSetUpApplicationComponent
import com.example.projectsetup.di.components.SetUpApplicationComponent
import com.example.projectsetup.di.modules.helper.ContextModule
import com.jakewharton.threetenabp.AndroidThreeTen
import es.dmoral.toasty.Toasty
import io.github.inflationx.viewpump.ViewPump
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

class SetUpApp :Application(), LifecycleObserver {

    private lateinit var component:SetUpApplicationComponent

    private lateinit var toast: Toast

    companion object {
        const val defaultOffset = 0
        var isAppInForeground = false
        var shouldShowNewOnBoarding = true
        const val APP_OPEN_WORKER = "APP_OPEN_WORKER"

        fun get(activity: Activity): SetUpApp {
            return activity.application as SetUpApp
        }

        fun get(service: Service): SetUpApp {
            return service.application as SetUpApp
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
        component = DaggerSetUpApplicationComponent.builder()
            .contextModule(ContextModule(this))
            .build()
    }

    private fun listenNoInternetEvent() {
        EventBus.getDefault().register(this)
    }

    fun setUpApplicationComponent(): SetUpApplicationComponent{
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