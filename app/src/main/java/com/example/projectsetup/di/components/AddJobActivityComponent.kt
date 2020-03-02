package com.example.projectsetup.di.components

import com.example.projectsetup.di.modules.activity.AddJobActivityModule
import com.example.projectsetup.di.modules.helper.ViewModelFactoryModule
import com.example.projectsetup.di.modules.helper.ViewModelModule
import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.admin.AddBranchActivity
import com.example.projectsetup.ui.admin.AddJobActivity
import dagger.Component

@Component(
    modules = [AddJobActivityModule::class,ViewModelFactoryModule::class, ViewModelModule::class],
    dependencies = [StudentHelperApplicationComponent::class]
)
@PerFragmentScope
interface AddJobActivityComponent {
    fun injectAddJobActivity(addJobActivity: AddJobActivity)
}