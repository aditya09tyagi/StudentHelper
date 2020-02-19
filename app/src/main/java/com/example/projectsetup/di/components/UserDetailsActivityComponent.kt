package com.example.projectsetup.di.components

import com.example.projectsetup.di.modules.activity.UserDetailsActivityModule
import com.example.projectsetup.di.modules.helper.ViewModelFactoryModule
import com.example.projectsetup.di.modules.helper.ViewModelModule
import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.user_details.UserDetailsActivity
import dagger.Component

@PerFragmentScope
@Component(
    modules = [UserDetailsActivityModule::class, ViewModelFactoryModule::class, ViewModelModule::class],
    dependencies = [StudentHelperApplicationComponent::class]
)
interface UserDetailsActivityComponent {
    fun injectUserDetailsActivityComponent(userDetailsActivity: UserDetailsActivity)
}