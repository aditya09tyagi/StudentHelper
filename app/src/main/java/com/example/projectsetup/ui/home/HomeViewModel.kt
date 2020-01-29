package com.example.projectsetup.ui.home

import androidx.lifecycle.ViewModel
import com.example.projectsetup.data.network.SetUpRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val setUpRepository: SetUpRepository
):ViewModel(){

}