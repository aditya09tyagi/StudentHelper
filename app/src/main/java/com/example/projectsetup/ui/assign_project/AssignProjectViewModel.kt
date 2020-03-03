package com.example.projectsetup.ui.assign_project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectsetup.data.models.*
import com.example.projectsetup.data.network.StudentHelperRepository
import javax.inject.Inject

class AssignProjectViewModel @Inject constructor(
    private val studentHelperRepository: StudentHelperRepository
) : ViewModel(), StudentHelperRepository.OnAssignProjectListener,
    StudentHelperRepository.OnSearchUserListener {

    private val _assignProjectLiveData = MutableLiveData<Resource<AssignProject>>()
    val assignProjectLiveData: LiveData<Resource<AssignProject>>
        get() = _assignProjectLiveData

    private val _searchUserLiveData = MutableLiveData<Resource<ArrayList<User>>>()
    val searchUserLiveData: LiveData<Resource<ArrayList<User>>>
        get() = _searchUserLiveData

    fun searchUser(queryText: String) {
        _searchUserLiveData.postValue(Resource.loading())
        studentHelperRepository.searchUsers(queryText,this)
    }

    fun assignProject(
        facultyId: String,
        title: String,
        description: String,
        commaSeparatedMemberIds: String,
        dayOfMonth: Int,
        month: Int,
        year: Int
    ) {
        _assignProjectLiveData.postValue(Resource.loading())
        studentHelperRepository.assignProject(
            facultyId = facultyId,
            title = title,
            description = description,
            commaSeparatedMemberIds = commaSeparatedMemberIds,
            dayOfMonth = dayOfMonth,
            month = month,
            year = year,
            onAssignProjectListener = this
        )
    }

    override fun onAssignProjectSuccess(project: AssignProject) {
        _assignProjectLiveData.postValue(Resource.success(project))
    }

    override fun onAssignProjectFailure(error: Error) {
        _assignProjectLiveData.postValue(Resource.error(error))
    }

    override fun onSearchUserSuccess(userList: ArrayList<User>) {
        _searchUserLiveData.postValue(Resource.success(userList))
    }

    override fun onSearchUserFailure(error: Error) {
        _searchUserLiveData.postValue(Resource.error(error))
    }
}