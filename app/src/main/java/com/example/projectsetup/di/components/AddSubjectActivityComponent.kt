package com.example.projectsetup.di.components

import com.example.projectsetup.di.modules.helper.ViewModelFactoryModule
import com.example.projectsetup.di.modules.helper.ViewModelModule
import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.admin.AddBranchActivity
import com.example.projectsetup.ui.admin.AddSubjectActivity
import dagger.Component

@Component(
    modules = [ViewModelFactoryModule::class, ViewModelModule::class],
    dependencies = [StudentHelperApplicationComponent::class]
)
@PerFragmentScope
interface AddSubjectActivityComponent {
    fun injectAddSubjectActivity(addSubjectActivity: AddSubjectActivity)
}