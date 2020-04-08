package com.example.projectsetup.di.modules.helper

import android.content.Context
import com.example.projectsetup.di.scopes.StudentHelperApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ContextModule(val context: Context) {

    @Provides
    @StudentHelperApplicationScope
    fun context(): Context {
        return context
    }
}