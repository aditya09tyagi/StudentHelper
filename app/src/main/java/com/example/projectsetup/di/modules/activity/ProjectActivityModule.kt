package com.example.projectsetup.di.modules.activity

import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.project.rv_project.ProjectAdapter
import dagger.Module
import dagger.Provides

@Module
class ProjectActivityModule {

    @Provides
    @PerFragmentScope
    fun setProjectAdapter():ProjectAdapter{
        return ProjectAdapter()
    }
}