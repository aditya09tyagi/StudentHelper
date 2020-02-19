package com.example.projectsetup.di.modules.activity

import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.user_details.SkillsAdapter
import dagger.Module
import dagger.Provides

@Module
class UserDetailsActivityModule {

    @Provides
    @PerFragmentScope
    fun setSkillAdapter():SkillsAdapter{
        return SkillsAdapter()
    }
}