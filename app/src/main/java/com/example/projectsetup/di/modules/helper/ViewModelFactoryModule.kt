package com.example.projectsetup.di.modules.helper

import androidx.lifecycle.ViewModelProvider
import com.example.projectsetup.util.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory
}