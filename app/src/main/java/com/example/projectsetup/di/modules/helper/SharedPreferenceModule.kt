package com.example.projectsetup.di.modules.helper

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.projectsetup.di.modules.network.NetworkModule
import com.example.projectsetup.di.scopes.SetUpApplicationScope
import com.example.projectsetup.util.SharedPreferenceUtil
import com.example.projectsetup.util.SharedPreferencesUserLiveData
import com.google.gson.Gson
import dagger.Module
import dagger.Provides

@Module(includes = [ContextModule::class, NetworkModule::class])
class SharedPreferenceModule {

    @Provides
    fun sharedPreferenceUtil(preferences: SharedPreferences, gson: Gson): SharedPreferenceUtil {
        return SharedPreferenceUtil(preferences, gson)
    }

    @Provides
    @SetUpApplicationScope
    fun preferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @SetUpApplicationScope
    fun getSharedPreferencesUserLiveData(preferences: SharedPreferences, gson: Gson): SharedPreferencesUserLiveData {
        return SharedPreferencesUserLiveData(preferences, gson)
    }

}