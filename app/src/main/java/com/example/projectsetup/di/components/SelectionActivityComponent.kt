package com.example.projectsetup.di.components

import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.selection.SelectionActivity
import dagger.Component

@PerFragmentScope
@Component(dependencies = [StudentHelperApplicationComponent::class])
interface SelectionActivityComponent {
    fun injectSelectionActivityComponent(selectionActivity: SelectionActivity)
}