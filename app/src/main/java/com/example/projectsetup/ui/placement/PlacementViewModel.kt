package com.example.projectsetup.ui.placement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectsetup.data.models.Error
import com.example.projectsetup.data.models.Resource
import com.example.projectsetup.data.models.Upcoming
import com.example.projectsetup.data.network.StudentHelperRepository
import javax.inject.Inject

class PlacementViewModel @Inject constructor(
    private val studentHelperRepository: StudentHelperRepository
) : ViewModel(), StudentHelperRepository.OnUpcomingCompaniesListener {

    private val _upcomingLiveData = MutableLiveData<Resource<ArrayList<Upcoming>>>()
    val upcomingLiveData: LiveData<Resource<ArrayList<Upcoming>>>
        get() = _upcomingLiveData

    fun upcomingCompanies(userId:String){
        _upcomingLiveData.postValue(Resource.loading())
        studentHelperRepository.upcomingCompanies(userId,this)
    }

    override fun onUpcomingCompanySuccess(upcoming: ArrayList<Upcoming>) {
        _upcomingLiveData.postValue(Resource.success(upcoming))
    }

    override fun onUpcomingCompanyFailure(error: Error) {
        _upcomingLiveData.postValue(Resource.error(error))
    }

}