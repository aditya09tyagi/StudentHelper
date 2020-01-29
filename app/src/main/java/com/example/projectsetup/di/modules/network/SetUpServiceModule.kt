@file:Suppress("unused")

package com.example.projectsetup.di.modules.network

import com.example.projectsetup.data.network.SetUpService
import com.example.projectsetup.di.scopes.SetUpApplicationScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [NetworkModule::class])
class SetUpServiceModule {

    companion object {
        private const val BASE_URL_TESTING = "https://apitesting.instasolv.com:3001"
        private const val BASE_URL_TESTING_NEW = "http://164.52.193.217:3000"
        private const val BASE_URL_RELEASE = "https://api.instasolv.com"
        private const val BASE_URL = BASE_URL_RELEASE
    }

    @Provides
    @SetUpApplicationScope
    fun setUpService(retrofit: Retrofit): SetUpService {
        return retrofit.create(SetUpService::class.java)
    }

    @Provides
    @SetUpApplicationScope
    fun retrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .build()
    }

    @Provides
    @SetUpApplicationScope
    fun gson(): Gson {
        return GsonBuilder().create()
    }
}