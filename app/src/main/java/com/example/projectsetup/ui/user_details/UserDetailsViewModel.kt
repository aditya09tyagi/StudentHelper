package com.example.projectsetup.ui.user_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectsetup.data.models.*
import com.example.projectsetup.data.network.StudentHelperRepository
import com.onesignal.OneSignal
import javax.inject.Inject

class UserDetailsViewModel @Inject constructor(
    private val studentHelperRepository: StudentHelperRepository
) : ViewModel(), StudentHelperRepository.OnSearchSkillListener,
    StudentHelperRepository.OnUpdateUserListener, StudentHelperRepository.OnGetAllBranchesListener {


    private val _skillsLiveData = MutableLiveData<Resource<ArrayList<Skill>>>()
    val skillsLiveData: LiveData<Resource<ArrayList<Skill>>>
        get() = _skillsLiveData

    private val _updateUserLiveData = MutableLiveData<Resource<User>>()
    val updateUserLiveData: LiveData<Resource<User>>
        get() = _updateUserLiveData

    private val _branchLiveData = MutableLiveData<Resource<List<Branch>>>()
    val branchLiveData: LiveData<Resource<List<Branch>>>
        get() = _branchLiveData


    fun searchSkills(queryText: String) {
        _skillsLiveData.postValue(Resource.loading())
        studentHelperRepository.searchSkills(queryText, this)
    }

    fun getBranches() {
        _branchLiveData.postValue(Resource.loading())
        studentHelperRepository.getAllBranches(this)
    }

    fun updateUser(
        userId: String,
        age: Int,
        branch: String,
        section: String,
        semester: Int,
        commaSeparatedSkillIds: String,
        userType: Int
    ) {
        val status = OneSignal.getPermissionSubscriptionState()
        var playerId = ""
        status?.let {
            it.subscriptionStatus?.let {
                it.userId?.let { id ->
                    playerId = id
                }
            }
        }

        studentHelperRepository.updateUser(
            userId = userId,
            age = age,
            branch = branch,
            section = section,
            semester = semester,
            commaSeparatedSkillIds = commaSeparatedSkillIds,
            userType = userType,
            playerId = playerId,
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

    override fun onGetAllBranchesSuccess(branches: ArrayList<Branch>) {
        _branchLiveData.postValue(Resource.success(branches))
    }

    override fun onGetAllBranchesFailure(error: Error) {
        _branchLiveData.postValue(Resource.error(error))
    }
}