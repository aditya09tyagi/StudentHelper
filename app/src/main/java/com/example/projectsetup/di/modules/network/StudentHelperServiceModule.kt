@file:Suppress("unused")

package com.example.projectsetup.di.modules.network

import com.example.projectsetup.data.network.StudentHelperService
import com.example.projectsetup.di.scopes.StudentHelperApplicationScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [NetworkModule::class])
class StudentHelperServiceModule {

    companion object {
        private const val BASE_URL_TESTING = "http://apitesting.instasolv.com:6000/"
        private const val BASE_URL_RELEASE = "https://guarded-harbor-78775.herokuapp.com"
        private const val BASE_URL = BASE_URL_RELEASE
    }

    @Provides
    @StudentHelperApplicationScope
    fun setUpService(retrofit: Retrofit): StudentHelperService {
        return retrofit.create(StudentHelperService::class.java)
    }

    @Provides
    @StudentHelperApplicationScope
    fun retrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .build()
    }

    @Provides
    @StudentHelperApplicationScope
    fun gson(): Gson {
        return GsonBuilder().create()
    }
}