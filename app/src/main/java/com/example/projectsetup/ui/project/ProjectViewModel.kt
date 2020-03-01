package com.example.projectsetup.ui.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectsetup.data.models.Error
import com.example.projectsetup.data.models.Project
import com.example.projectsetup.data.models.Resource
import com.example.projectsetup.data.network.StudentHelperRepository
import javax.inject.Inject

class ProjectViewModel @Inject constructor(
    private val studentHelperRepository: StudentHelperRepository
) : ViewModel(), StudentHelperRepository.OnGetMyProjectListener {


    private val _projectLiveData = MutableLiveData<Resource<Project>>()
    val projectLiveData: LiveData<Resource<Project>>
        get() = _projectLiveData

    fun getMyProject(userId: String) {
        _projectLiveData.postValue(Resource.loading())
        studentHelperRepository.getMyProject(userId,this)
    }

    override fun onGetMyProjectSuccess(project: Project) {
        _projectLiveData.postValue(Resource.success(project))
    }

    override fun onGetMyProjectFailure(error: Error) {
        _projectLiveData.postValue(Resource.error(error))
    }
}