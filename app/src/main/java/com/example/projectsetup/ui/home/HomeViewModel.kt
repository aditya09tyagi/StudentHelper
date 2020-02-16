package com.example.projectsetup.ui.home

import androidx.lifecycle.ViewModel
import com.example.projectsetup.data.network.StudentHelperRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val studentHelperRepository: StudentHelperRepository
):ViewModel(){

}