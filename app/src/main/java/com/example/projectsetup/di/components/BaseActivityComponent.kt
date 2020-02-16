package com.example.projectsetup.di.components

import com.example.projectsetup.di.modules.BaseActivityModule
import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.base.BaseActivity
import dagger.Component

@Component(modules = [BaseActivityModule::class], dependencies = [StudentHelperApplicationComponent::class])
@PerFragmentScope
interface BaseActivityComponent {

    fun injectBaseActivity(baseActivity: BaseActivity)

}