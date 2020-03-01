package com.example.projectsetup.di.modules.helper

import androidx.lifecycle.ViewModel
import com.example.projectsetup.di.mapkey.ViewModelKey
import com.example.projectsetup.ui.chat.ChatViewModel
import com.example.projectsetup.ui.home.HomeViewModel
import com.example.projectsetup.ui.login.LoginViewModel
import com.example.projectsetup.ui.placement.PlacementViewModel
import com.example.projectsetup.ui.project.ProjectViewModel
import com.example.projectsetup.ui.splash.SplashViewModel
import com.example.projectsetup.ui.user_details.UserDetailsViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@Suppress("unused")
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(splashViewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserDetailsViewModel::class)
    abstract fun bindUserDetailsViewModel(userDetailsViewModel: UserDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlacementViewModel::class)
    abstract fun bindPlacementViewModel(placementViewModel: PlacementViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProjectViewModel::class)
    abstract fun bindProjectViewModel(projectViewModel: ProjectViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    abstract fun bindChatViewModel(chatViewModel: ChatViewModel):ViewModel
}