package com.example.projectsetup.di.modules.application

import android.content.Context
import com.example.projectsetup.R
import com.example.projectsetup.di.modules.helper.ContextModule
import com.example.projectsetup.di.scopes.StudentHelperApplicationScope
import com.example.projectsetup.ui.notification.NotificationOpenedHandler
import com.onesignal.OneSignal
import dagger.Module
import dagger.Provides
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import timber.log.Timber

@Module(includes = [ContextModule::class])
class StudentHelperAppModule {

    @Provides
    @StudentHelperApplicationScope
    fun timberTree(): Timber.Tree {
        return Timber.DebugTree()
    }

    @Provides
    @StudentHelperApplicationScope
    fun calligraphyInterceptor(): CalligraphyInterceptor {
        return CalligraphyInterceptor(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Lato.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
    }

    @Provides
    @StudentHelperApplicationScope
    fun oneSignalBuilder(
        context: Context,
        notificationOpenedHandler: NotificationOpenedHandler
    ): OneSignal.Builder {
        return OneSignal.startInit(context)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .setNotificationOpenedHandler(notificationOpenedHandler)
    }


    @Provides
    @StudentHelperApplicationScope
    fun notificationOpenedHandler(
        context: Context
    ): NotificationOpenedHandler {
        return NotificationOpenedHandler(context)
    }
}