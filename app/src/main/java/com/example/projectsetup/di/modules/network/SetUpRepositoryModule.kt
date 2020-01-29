package com.example.projectsetup.di.modules.network

import com.example.projectsetup.data.network.SetUpRepository
import com.example.projectsetup.data.network.SetUpService
import com.example.projectsetup.di.scopes.SetUpApplicationScope
import dagger.Module
import dagger.Provides

@Module(includes = [SetUpServiceModule::class])
class SetUpRepositoryModule {

    @Provides
    @SetUpApplicationScope
    fun setUpRepository(setUpService: SetUpService): SetUpRepository {
        return SetUpRepository(setUpService)
    }
}