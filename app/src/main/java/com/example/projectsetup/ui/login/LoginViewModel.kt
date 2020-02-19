package com.example.projectsetup.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectsetup.data.models.Error
import com.example.projectsetup.data.models.Resource
import com.example.projectsetup.data.models.User
import com.example.projectsetup.data.network.StudentHelperRepository
import com.example.projectsetup.util.Constants
import com.example.projectsetup.util.SharedPreferenceUtil
import com.example.projectsetup.util.StringUtils
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val studentHelperRepository: StudentHelperRepository,
    private val sharedPreferenceUtil: SharedPreferenceUtil,
    private val gson: Gson
) : ViewModel(), StudentHelperRepository.OnLoginListener {

    private var avatarUrl: String? = null

    private val _userLiveData = MutableLiveData<Resource<User>>()
    val userLiveData: LiveData<Resource<User>>
        get() = _userLiveData


    fun handleGoogleSignInResult(googleSignInTask: Task<GoogleSignInAccount>) {
        try {
            val account = googleSignInTask.getResult(ApiException::class.java)
            account?.let {
                val name = it.displayName
                val email = it.email
                avatarUrl = it.photoUrl.toString()
                if (email!!.contains("abes")) {
                    login(name!!, email, avatarUrl)
                } else {
                    _userLiveData.postValue(
                        Resource.error(
                            Error(
                                false,
                                "Please use your college id to login."
                            )
                        )
                    )
                }
            }
        } catch (e: ApiException) {
            e.localizedMessage?.let { message ->
                if (e.statusCode != 12501)
                    _userLiveData.postValue(Resource.error(Error(false, message)))
                else if (e.statusCode == 7)
                    _userLiveData.postValue(Resource.error(Error(false, message)))
            } ?: run {
                _userLiveData.postValue(Resource.error())
            }
        }
    }

    private fun login(name: String, email: String, avatarUrl: String?) {
        _userLiveData.postValue(Resource.loading())
        studentHelperRepository.login(name, email, avatarUrl, this)
    }

    override fun onLoginSuccess(response: Response<User>) {
        val user = response.body()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                sharedPreferenceUtil.putString(Constants.EXTRA_USER, gson.toJson(user))
            }
            _userLiveData.postValue(Resource.success(user))
        }
    }

    override fun onLoginFailure(error: Error) {
        _userLiveData.postValue(Resource.error(error))
    }
}