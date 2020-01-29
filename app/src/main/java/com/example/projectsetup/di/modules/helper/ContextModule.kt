package com.example.projectsetup.di.modules.helper

import android.content.Context
import com.example.projectsetup.di.scopes.SetUpApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ContextModule(val context: Context) {

    @Provides
    @SetUpApplicationScope
    fun context(): Context {
        return context
    }
}