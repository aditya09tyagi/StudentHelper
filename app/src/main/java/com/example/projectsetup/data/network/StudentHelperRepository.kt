package com.example.projectsetup.data.network

import com.example.projectsetup.data.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

class StudentHelperRepository (private val studentHelperService: StudentHelperService){

    fun login(name: String, email: String, avatarUrl: String? = null, onLoginListener: OnLoginListener) {
        studentHelperService.login(name, email, avatarUrl).enqueue(object : Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    response.body()?.let { _ ->
                        onLoginListener.onLoginSuccess(response)
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                if (t is UnknownHostException) {
                    onLoginListener.onLoginFailure(Error(true, "Internet not connected"))
                } else {
                    onLoginListener.onLoginFailure(Error(true, "${t.message}"))
                }
            }
        })
    }

    //-------INTERFACES----------


    interface OnLoginListener {

        fun onLoginSuccess(response: Response<User>)

        fun onLoginFailure(error: Error)

    }
}