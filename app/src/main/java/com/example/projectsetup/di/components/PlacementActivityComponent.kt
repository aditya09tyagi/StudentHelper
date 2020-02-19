package com.example.projectsetup.di.components

import com.example.projectsetup.di.modules.activity.PlacementActivityModule
import com.example.projectsetup.di.modules.helper.ViewModelFactoryModule
import com.example.projectsetup.di.modules.helper.ViewModelModule
import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.placement.PlacementActivity
import dagger.Component

@PerFragmentScope
@Component(
    modules = [PlacementActivityModule::class, ViewModelFactoryModule::class, ViewModelModule::class],
    dependencies = [StudentHelperApplicationComponent::class]
)
interface PlacementActivityComponent {
    fun injectPlacementActivityComponent(placementActivity: PlacementActivity)
}