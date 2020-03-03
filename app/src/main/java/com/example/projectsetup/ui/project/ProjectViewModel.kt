package com.example.projectsetup.ui.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectsetup.data.models.*
import com.example.projectsetup.data.network.StudentHelperRepository
import javax.inject.Inject

class ProjectViewModel @Inject constructor(
    private val studentHelperRepository: StudentHelperRepository
) : ViewModel(), StudentHelperRepository.OnGetMyProjectListener,
    StudentHelperRepository.OnGetProjectUnderFacultyListener,
    StudentHelperRepository.OnUpdateProjectProgressListener {

    private val _projectLiveData = MutableLiveData<Resource<ArrayList<Project>>>()
    val projectLiveData: LiveData<Resource<ArrayList<Project>>>
        get() = _projectLiveData

    private val _facultyProjectLiveData = MutableLiveData<Resource<ArrayList<Project>>>()
    val facultyProjectLiveData: LiveData<Resource<ArrayList<Project>>>
        get() = _facultyProjectLiveData

    private val _updateProgressLiveData = MutableLiveData<Resource<Project>>()
    val updateProgressLiveData: LiveData<Resource<Project>>
        get() = _updateProgressLiveData

    fun getMyProject(userId: String) {
        _projectLiveData.postValue(Resource.loading())
        studentHelperRepository.getMyProject(userId, this)
    }

    fun getFacultyProject(facultyId: String) {
        _facultyProjectLiveData.postValue(Resource.loading())
        studentHelperRepository.getProjectUnderFaculty(facultyId, this)
    }

    fun updateProjectProgress(progressValue: Int, projectId: String) {
        _updateProgressLiveData.postValue(Resource.loading())
        studentHelperRepository.updateProjectProgress(progressValue, projectId, this)
    }

    override fun onGetMyProjectSuccess(project: ArrayList<Project>) {
        _projectLiveData.postValue(Resource.success(project))
    }

    override fun onGetMyProjectFailure(error: Error) {
        _projectLiveData.postValue(Resource.error(error))
    }

    override fun onGetProjectUnderFacultySuccess(project: ArrayList<Project>) {
        _facultyProjectLiveData.postValue(Resource.success(project))
    }

    override fun onGetProjectUnderFacultyFailure(error: Error) {
        _facultyProjectLiveData.postValue(Resource.error(error))
    }

    override fun onUpdateProjectProgressSuccess(updatedProject: Project) {
        _updateProgressLiveData.postValue(Resource.success(updatedProject))
    }

    override fun onUpdateProjectProgressFailure(error: Error) {
        _updateProgressLiveData.postValue(Resource.error(error))
    }

}