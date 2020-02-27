package com.example.projectsetup.di.components

import com.example.projectsetup.di.modules.activity.ChatActivityModule
import com.example.projectsetup.di.modules.helper.ViewModelFactoryModule
import com.example.projectsetup.di.modules.helper.ViewModelModule
import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.chat.ChatActivity
import dagger.Component

@PerFragmentScope
@Component(
    modules = [ChatActivityModule::class, ViewModelFactoryModule::class, ViewModelModule::class],
    dependencies = [StudentHelperApplicationComponent::class]
)
interface ChatActivityComponent {
    fun injectChatActivityComponent(chatActivity: ChatActivity)
}