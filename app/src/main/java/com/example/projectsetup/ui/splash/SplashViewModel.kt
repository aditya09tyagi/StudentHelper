package com.example.projectsetup.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectsetup.util.Constants
import com.example.projectsetup.util.SharedPreferenceUtil
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val sharedPreferenceUtil: SharedPreferenceUtil
) :
    ViewModel() {

    private val _isUserLoggedInLiveData = MutableLiveData<Boolean>()
    val isUserLoggedInLiveData: LiveData<Boolean>
        get() = _isUserLoggedInLiveData

    fun isUserLoggedIn() {
        _isUserLoggedInLiveData.postValue(
            sharedPreferenceUtil.getBoolean(
                Constants.IS_ALREADY_LOGGED_IN,
                false
            )
        )
    }
}