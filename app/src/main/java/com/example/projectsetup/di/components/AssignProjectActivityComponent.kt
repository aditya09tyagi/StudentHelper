package com.example.projectsetup.di.components

import com.example.projectsetup.di.modules.activity.AssignProjectActivityModule
import com.example.projectsetup.di.modules.helper.ViewModelFactoryModule
import com.example.projectsetup.di.modules.helper.ViewModelModule
import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.assign_project.AssignProjectActivity
import dagger.Component

@PerFragmentScope
@Component(
    modules = [AssignProjectActivityModule::class, ViewModelFactoryModule::class, ViewModelModule::class],
    dependencies = [StudentHelperApplicationComponent::class]
)
interface AssignProjectActivityComponent {
    fun injectAssignProjectActivity(assignProjectActivity: AssignProjectActivity)
}