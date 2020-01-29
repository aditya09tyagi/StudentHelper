package com.example.projectsetup.di.components

import com.example.projectsetup.data.network.SetUpRepository
import com.example.projectsetup.di.modules.application.SetUpAppModule
import com.example.projectsetup.di.modules.helper.SharedPreferenceModule
import com.example.projectsetup.di.modules.helper.UtilsModule
import com.example.projectsetup.di.modules.libraries.PicassoModule
import com.example.projectsetup.di.modules.network.SetUpRepositoryModule
import com.example.projectsetup.di.scopes.SetUpApplicationScope
import com.example.projectsetup.util.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import dagger.Component
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import timber.log.Timber

@SetUpApplicationScope
@Component(modules = [
    SetUpAppModule::class,
    SetUpRepositoryModule::class,
    PicassoModule::class,
    SharedPreferenceModule::class,
    UtilsModule::class
])
interface SetUpApplicationComponent {

    fun calligraphyInterceptor(): CalligraphyInterceptor

    fun timberTree(): Timber.Tree

    fun getPicasso(): Picasso

    fun getSetUpRepository(): SetUpRepository

    fun getSharedPreferenceUtil(): SharedPreferenceUtil

    fun getSharedPreferencesUserLiveData(): SharedPreferencesUserLiveData

    fun getGson(): Gson

    fun getStorageUtil(): StorageUtils

    fun getMultipartUtil(): MultipartUtils

    fun analyticsUtil(): AnalyticsUtil

}