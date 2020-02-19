package com.example.projectsetup.di.modules.activity

import android.content.Context
import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.login.LoginAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides

@Module
class LoginActivityModule(val context: Context) {

    @Provides
    @PerFragmentScope
    fun setLoginAdapter():LoginAdapter{
        return LoginAdapter()
    }

    @Provides
    @PerFragmentScope
    fun googleSignInClient(googleSignInOptions: GoogleSignInOptions): GoogleSignInClient {
        return GoogleSignIn.getClient(context, googleSignInOptions)
    }

    @Provides
    @PerFragmentScope
    fun googleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("241504643600-7mvciklkncb1taudo6h2f41in0agg18e.apps.googleusercontent.com")
            .requestEmail()
            .build()
    }

}