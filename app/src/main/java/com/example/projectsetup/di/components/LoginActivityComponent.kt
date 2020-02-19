package com.example.projectsetup.di.components

import com.example.projectsetup.di.modules.activity.LoginActivityModule
import com.example.projectsetup.di.modules.helper.ViewModelFactoryModule
import com.example.projectsetup.di.modules.helper.ViewModelModule
import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.login.LoginActivity
import dagger.Component

@PerFragmentScope
@Component(
    modules = [LoginActivityModule::class,ViewModelFactoryModule::class, ViewModelModule::class],
    dependencies = [StudentHelperApplicationComponent::class]
)
interface LoginActivityComponent {
    fun injectLoginActivityComponent(loginActivity: LoginActivity)
}