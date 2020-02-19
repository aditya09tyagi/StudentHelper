package com.example.projectsetup.data.network

import com.example.projectsetup.data.models.Skill
import com.example.projectsetup.data.models.Upcoming
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

    @GET("v1/home/searchSkill")
    fun searchSkill(
        @Query("text") queryText: String
    ): Call<ArrayList<Skill>>

    @GET("v1/user/updateUser")
    fun updateUser(
        @Query("userId") userId: String,
        @Query("age") age: String?,
        @Query("branch") branch: String?,
        @Query("userType") userType: String?,
        @Query("section") section: String?,
        @Query("semester") semester: String?,
        @Query("skills") commaSeparatedSkillIds: String?
    ): Call<User>

    @GET("/placement/schedule/upcoming")
    fun upcomingCompanies(
        @Query("userId") userId: String
    ):Call<ArrayList<Upcoming>>
}