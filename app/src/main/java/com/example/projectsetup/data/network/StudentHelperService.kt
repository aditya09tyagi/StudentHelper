package com.example.projectsetup.data.network

import com.example.projectsetup.data.models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface StudentHelperService {

    @GET("/v1/user/login")
    fun login(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("avatar_url") avatarUrl: String?
    ): Call<User>
}