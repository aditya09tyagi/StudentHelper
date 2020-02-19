package com.example.projectsetup.di.components

import com.example.projectsetup.di.modules.activity.HomeActivityModule
import com.example.projectsetup.di.modules.helper.ViewModelFactoryModule
import com.example.projectsetup.di.modules.helper.ViewModelModule
import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.home.HomeActivity
import dagger.Component

@PerFragmentScope
@Component(
    modules = [HomeActivityModule::class, ViewModelFactoryModule::class, ViewModelModule::class],
    dependencies = [StudentHelperApplicationComponent::class]
)
interface HomeActivityComponent {
    fun injectHomeActivity(homeActivity: HomeActivity)
}