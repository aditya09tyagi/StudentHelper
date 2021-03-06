package com.example.projectsetup.di.components

import com.example.projectsetup.data.network.StudentHelperRepository
import com.example.projectsetup.di.modules.application.StudentHelperAppModule
import com.example.projectsetup.di.modules.helper.SharedPreferenceModule
import com.example.projectsetup.di.modules.helper.UtilsModule
import com.example.projectsetup.di.modules.libraries.PicassoModule
import com.example.projectsetup.di.modules.network.StudentHelperRepositoryModule
import com.example.projectsetup.di.scopes.StudentHelperApplicationScope
import com.example.projectsetup.util.*
import com.google.gson.Gson
import com.onesignal.OneSignal
import com.squareup.picasso.Picasso
import dagger.Component
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import timber.log.Timber

@StudentHelperApplicationScope
@Component(modules = [
    StudentHelperAppModule::class,
    StudentHelperRepositoryModule::class,
    PicassoModule::class,
    SharedPreferenceModule::class,
    UtilsModule::class
])
interface StudentHelperApplicationComponent {

    fun calligraphyInterceptor(): CalligraphyInterceptor

    fun timberTree(): Timber.Tree

    fun oneSignalBuilder(): OneSignal.Builder

    fun getPicasso(): Picasso

    fun getStudentHelperRepository(): StudentHelperRepository

    fun getSharedPreferenceUtil(): SharedPreferenceUtil

    fun getSharedPreferencesUserLiveData(): SharedPreferencesUserLiveData

    fun getGson(): Gson

    fun getStorageUtil(): StorageUtils

    fun getMultipartUtil(): MultipartUtils

    fun analyticsUtil(): AnalyticsUtil

}