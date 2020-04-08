package com.example.projectsetup.di.modules.network

import com.example.projectsetup.data.network.StudentHelperRepository
import com.example.projectsetup.data.network.StudentHelperService
import com.example.projectsetup.di.scopes.StudentHelperApplicationScope
import dagger.Module
import dagger.Provides

@Module(includes = [StudentHelperServiceModule::class])
class StudentHelperRepositoryModule {

    @Provides
    @StudentHelperApplicationScope
    fun setUpRepository(studentHelperService: StudentHelperService): StudentHelperRepository {
        return StudentHelperRepository(studentHelperService)
    }
}