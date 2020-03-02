package com.example.projectsetup.di.components

import com.example.projectsetup.di.modules.activity.AddBranchActivityModule
import com.example.projectsetup.di.modules.helper.ViewModelFactoryModule
import com.example.projectsetup.di.modules.helper.ViewModelModule
import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.admin.AddBranchActivity
import dagger.Component

@Component(
    modules = [AddBranchActivityModule::class,ViewModelFactoryModule::class, ViewModelModule::class],
    dependencies = [StudentHelperApplicationComponent::class]
)
@PerFragmentScope
interface AddBranchActivityComponent {
    fun injectAddBranchActivity(addBranchActivity: AddBranchActivity)
}