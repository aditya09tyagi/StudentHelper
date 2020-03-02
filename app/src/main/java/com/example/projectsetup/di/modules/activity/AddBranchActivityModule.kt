package com.example.projectsetup.di.modules.activity

import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.generic_rv.GenericDataAdapter
import dagger.Module
import dagger.Provides

@Module
class AddBranchActivityModule {

    @Provides
    @PerFragmentScope
    fun setGenericDataAdapter(): GenericDataAdapter {
        return GenericDataAdapter()
    }
}