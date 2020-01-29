package com.example.projectsetup.di.modules.libraries

import android.content.Context
import com.example.projectsetup.di.modules.helper.ContextModule
import com.example.projectsetup.di.scopes.SetUpApplicationScope
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides

@Module(includes = [ContextModule::class])
class PicassoModule {

    @Provides
    @SetUpApplicationScope
    fun picasso(context: Context): Picasso {
        return Picasso.Builder(context)
                .build()
    }
}