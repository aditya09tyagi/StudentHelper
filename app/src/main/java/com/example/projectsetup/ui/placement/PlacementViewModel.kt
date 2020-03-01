package com.example.projectsetup.ui.placement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectsetup.data.models.Error
import com.example.projectsetup.data.models.Past
import com.example.projectsetup.data.models.Resource
import com.example.projectsetup.data.models.Upcoming
import com.example.projectsetup.data.network.StudentHelperRepository
import javax.inject.Inject

class PlacementViewModel @Inject constructor(
    private val studentHelperRepository: StudentHelperRepository
) : ViewModel(), StudentHelperRepository.OnUpcomingCompaniesListener,
    StudentHelperRepository.OnPastCompaniesListener {

    private val _upcomingLiveData = MutableLiveData<Resource<ArrayList<Upcoming>>>()
    val upcomingLiveData: LiveData<Resource<ArrayList<Upcoming>>>
        get() = _upcomingLiveData

    private val _pastLiveData = MutableLiveData<Resource<ArrayList<Past>>>()
    val pastLiveData: LiveData<Resource<ArrayList<Past>>>
        get() = _pastLiveData

    fun upcomingCompanies(userId:String){
        _upcomingLiveData.postValue(Resource.loading())
        studentHelperRepository.getUpcomingCompanies(userId,this)
    }

    fun pastCompanies(userId:String){
        _pastLiveData.postValue(Resource.loading())
        studentHelperRepository.getPastCompanies(userId,this)
    }

    override fun onUpcomingCompanySuccess(upcoming: ArrayList<Upcoming>) {
        _upcomingLiveData.postValue(Resource.success(upcoming))
    }

    override fun onUpcomingCompanyFailure(error: Error) {
        _upcomingLiveData.postValue(Resource.error(error))
    }

    override fun onPastCompanySuccess(past: ArrayList<Past>) {
        _pastLiveData.postValue(Resource.success(past))
    }

    override fun onPastCompanyFailure(error: Error) {
        _pastLiveData.postValue(Resource.error(error))
    }

}