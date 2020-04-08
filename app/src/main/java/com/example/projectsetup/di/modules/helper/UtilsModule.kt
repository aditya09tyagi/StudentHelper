package com.example.projectsetup.di.modules.helper

import android.content.Context
import com.example.projectsetup.di.modules.libraries.PicassoModule
import com.example.projectsetup.di.scopes.StudentHelperApplicationScope
import com.example.projectsetup.util.*
import dagger.Module
import dagger.Provides

@Module(includes = [ContextModule::class, PicassoModule::class])
class UtilsModule {

    @Provides
    @StudentHelperApplicationScope
    fun storageUtil(): StorageUtils {
        return StorageUtils()
    }

    @Provides
    @StudentHelperApplicationScope
    fun networkUtil(context: Context): NetworkUtil {
        return NetworkUtil(context)
    }

    @Provides
    @StudentHelperApplicationScope
    fun multipartUtil(context: Context): MultipartUtils {
        return MultipartUtils(context)
    }

    @Provides
    @StudentHelperApplicationScope
    fun analyticsUtil(
            context: Context
    ): AnalyticsUtil {
        return AnalyticsUtil(context)
    }

}