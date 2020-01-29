package com.example.projectsetup.di.modules.application

import android.content.Context
import com.example.projectsetup.R
import com.example.projectsetup.di.modules.helper.ContextModule
import com.example.projectsetup.di.scopes.SetUpApplicationScope
import dagger.Module
import dagger.Provides
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import timber.log.Timber

@Module(includes = [ContextModule::class])
class SetUpAppModule {

    @Provides
    @SetUpApplicationScope
    fun timberTree(): Timber.Tree {
        return Timber.DebugTree()
    }

    @Provides
    @SetUpApplicationScope
    fun calligraphyInterceptor(): CalligraphyInterceptor {
        return CalligraphyInterceptor(
                CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Lato.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        )
    }
}