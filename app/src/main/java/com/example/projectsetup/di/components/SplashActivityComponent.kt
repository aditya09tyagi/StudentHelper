package com.example.projectsetup.di.components

import com.example.projectsetup.di.modules.helper.ViewModelFactoryModule
import com.example.projectsetup.di.modules.helper.ViewModelModule
import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.splash.SplashActivity
import dagger.Component

@PerFragmentScope
@Component(
    modules = [ViewModelFactoryModule::class, ViewModelModule::class],
    dependencies = [StudentHelperApplicationComponent::class]
)
interface SplashActivityComponent {
    fun injectSplashActivityComponent(splashActivity: SplashActivity)
}