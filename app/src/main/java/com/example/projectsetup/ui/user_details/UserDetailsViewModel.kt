package com.example.projectsetup.ui.user_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectsetup.data.models.Error
import com.example.projectsetup.data.models.Resource
import com.example.projectsetup.data.models.Skill
import com.example.projectsetup.data.models.User
import com.example.projectsetup.data.network.StudentHelperRepository
import javax.inject.Inject

class UserDetailsViewModel @Inject constructor(
    private val studentHelperRepository: StudentHelperRepository
) : ViewModel(), StudentHelperRepository.OnSearchSkillListener,
    StudentHelperRepository.OnUpdateUserListener {


    private val _skillsLiveData = MutableLiveData<Resource<ArrayList<Skill>>>()
    val skillsLiveData: LiveData<Resource<ArrayList<Skill>>>
        get() = _skillsLiveData

    private val _updateUserLiveData = MutableLiveData<Resource<User>>()
    val updateUserLiveData: LiveData<Resource<User>>
        get() = _updateUserLiveData


    fun searchSkills(queryText: String) {
        _skillsLiveData.postValue(Resource.loading())
        studentHelperRepository.searchSkills(queryText, this)
    }

    fun updateUser(
        userId: String,
        age: String,
        branch: String,
        section: String,
        semester: String,
        commaSeparatedSkillIds: String
    ) {
        studentHelperRepository.updateUser(
            userId = userId,
            age = age,
//            branch = branch,
            section = section,
            semester = semester,
//            commaSeparatedSkillIds = commaSeparatedSkillIds,
            onUpdateUserListener = this
        )
    }

    override fun onSearchSkillSuccess(skills: ArrayList<Skill>) {
        _skillsLiveData.postValue(Resource.success(skills))
    }

    override fun onSearchSkillFailure(error: Error) {
        _skillsLiveData.postValue(Resource.error(error))
    }

    override fun onUpdateUserSuccess(user: User) {
        _updateUserLiveData.postValue(Resource.success(user))
    }

    override fun onUpdateUserFailure(error: Error) {
        _updateUserLiveData.postValue(Resource.error(error))
    }
}