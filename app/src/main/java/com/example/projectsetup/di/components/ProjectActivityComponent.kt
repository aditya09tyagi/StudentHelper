package com.example.projectsetup.di.components

import com.example.projectsetup.di.modules.activity.ProjectActivityModule
import com.example.projectsetup.di.modules.helper.ViewModelFactoryModule
import com.example.projectsetup.di.modules.helper.ViewModelModule
import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.project.ProjectActivity
import dagger.Component

@PerFragmentScope
@Component(
    modules = [ProjectActivityModule::class,ViewModelFactoryModule::class, ViewModelModule::class],
    dependencies = [StudentHelperApplicationComponent::class]
)
interface ProjectActivityComponent {
    fun injectProjectActivity(projectActivity: ProjectActivity)
}