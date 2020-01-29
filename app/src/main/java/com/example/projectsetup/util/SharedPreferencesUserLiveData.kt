package com.example.projectsetup.util;

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.projectsetup.data.models.User
import com.google.gson.Gson
import java.lang.Exception

class SharedPreferencesUserLiveData(
        sharedPrefs: SharedPreferences, private val gson: Gson
) :
        SharedPreferencesLiveData<User>(sharedPrefs) {

    fun getString(key: String, default: String): String {
        sharedPrefs.getString(key, default)?.let {
            return it
        } ?: run {
            return ""
        }
    }

    fun getString(key: String): String {
        return getString(key, "")
    }

    override fun getValueFromPreferences(key: String): User? {
        var user: User? = null
        try {
            val userString = getString(Constants.EXTRA_USER)
            if (StringUtils.checkValidString(userString)) {
                user = gson.fromJson(userString, User::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            return user
        }
    }
}

abstract class SharedPreferencesLiveData<T>(val sharedPrefs: SharedPreferences) : LiveData<T>() {
    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        if (key == Constants.EXTRA_USER) {
            value = getValueFromPreferences(key)
        }
    }

    abstract fun getValueFromPreferences(key: String): T?

    override fun onActive() {
        super.onActive()
        value = getValueFromPreferences(Constants.EXTRA_USER)
        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onInactive() {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
        super.onInactive()
    }
}