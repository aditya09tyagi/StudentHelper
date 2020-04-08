package com.example.projectsetup.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectsetup.data.models.*
import com.example.projectsetup.data.network.StudentHelperRepository
import com.onesignal.OneSignal
import javax.inject.Inject

class AdminActivityViewModel @Inject constructor(
    private val studentHelperRepository: StudentHelperRepository
) : ViewModel(),
    StudentHelperRepository.OnAddBranchListener,
    StudentHelperRepository.OnAddCompanyListener,
    StudentHelperRepository.OnAddJobListener,
    StudentHelperRepository.OnAddSkillListener,
    StudentHelperRepository.OnAddSubjectListener, StudentHelperRepository.OnGetAllSubjectsListener,
    StudentHelperRepository.OnSearchSkillListener,
    StudentHelperRepository.OnGetAllCompaniesListener,
    StudentHelperRepository.OnUpdateUserListener {

    private val _addBranchLiveData = MutableLiveData<Resource<Branch>>()
    val addBranchLiveData: LiveData<Resource<Branch>>
        get() = _addBranchLiveData

    private val _updateUserLiveData = MutableLiveData<Resource<User>>()
    val updateUserLiveData: LiveData<Resource<User>>
        get() = _updateUserLiveData

    private val _addCompanyLiveData = MutableLiveData<Resource<Company>>()
    val addCompanyLiveData: LiveData<Resource<Company>>
        get() = _addCompanyLiveData

    private val _addJobLiveData = MutableLiveData<Resource<Job>>()
    val addJobLiveData: LiveData<Resource<Job>>
        get() = _addJobLiveData

    private val _addSkillLiveData = MutableLiveData<Resource<Skill>>()
    val addSkillLiveData: LiveData<Resource<Skill>>
        get() = _addSkillLiveData

    private val _addSubjectLiveData = MutableLiveData<Resource<Subject>>()
    val addSubjectLiveData: LiveData<Resource<Subject>>
        get() = _addSubjectLiveData

    private val _getSubjectsLiveData = MutableLiveData<Resource<ArrayList<Subject>>>()
    val getSubjectLiveData: LiveData<Resource<ArrayList<Subject>>>
        get() = _getSubjectsLiveData

    private val _getSkillsLiveData = MutableLiveData<Resource<ArrayList<Skill>>>()
    val getSkillsLiveData: LiveData<Resource<ArrayList<Skill>>>
        get() = _getSkillsLiveData

    private val _getCompaniesLiveData = MutableLiveData<Resource<ArrayList<Company>>>()
    val getCompaniesLiveData: LiveData<Resource<ArrayList<Company>>>
        get() = _getCompaniesLiveData

    fun updateUser(
        userId: String,
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

        _updateUserLiveData.postValue(Resource.loading())

        studentHelperRepository.updateUser(
            userId = userId,
            userType = userType,
            playerId = playerId,
            onUpdateUserListener = this
        )
    }

    fun addBranch(
        branchName: String,
        commaSeparatedSubjectIds: String,
        userId: String
    ) {
        _addBranchLiveData.postValue(Resource.loading())
        studentHelperRepository.addBranch(branchName, commaSeparatedSubjectIds, userId, this)
    }

    fun addCompany(
        companyName: String,
        websiteLink: String,
        companyLogo: String,
        userId: String
    ) {
        _addCompanyLiveData.postValue(Resource.loading())
        studentHelperRepository.addCompany(companyName, websiteLink, companyLogo, userId, this)
    }

    fun addJob(
        companyId: String,
        jobTitle: String,
        jobDescription: String,
        driveLocation: String,
        commaSeparatedSkillIds: String,
        driveDay: Int,
        driveMonth: Int,
        driveYear: Int,
        driveHour: Int,
        driveMinute: Int,
        userId: String
    ) {
        _addJobLiveData.postValue(Resource.loading())
        studentHelperRepository.addJob(
            companyId,
            jobTitle,
            jobDescription,
            driveLocation,
            commaSeparatedSkillIds,
            driveDay,
            driveMonth,
            driveYear,
            driveHour,
            driveMinute,
            userId,
            this
        )
    }

    fun addSkill(skillName: String, userId: String) {
        _addSkillLiveData.postValue(Resource.loading())
        studentHelperRepository.addSkill(skillName, userId, this)
    }

    fun addSubject(subjectName: String, userID: String) {
        _addSubjectLiveData.postValue(Resource.loading())
        studentHelperRepository.addSubject(subjectName, userID, this)
    }

    fun getAllSubjects() {
        _getSubjectsLiveData.postValue(Resource.loading())
        studentHelperRepository.getAllSubjects(this)
    }

    fun searchSkills(queryText: String) {
        _getSkillsLiveData.postValue(Resource.loading())
        studentHelperRepository.searchSkills(queryText, this)
    }

    fun getAllCompanies() {
        _getCompaniesLiveData.postValue(Resource.loading())
        studentHelperRepository.getAllCompanies(this)
    }

    override fun onAddBranchSuccess(branch: Branch) {
        _addBranchLiveData.postValue(Resource.success(branch))
    }

    override fun onAddBranchFailure(error: Error) {
        _addBranchLiveData.postValue(Resource.error(error))
    }

    override fun onAddCompanySuccess(company: Company) {
        _addCompanyLiveData.postValue(Resource.success(company))
    }

    override fun onAddCompanyFailure(error: Error) {
        _addCompanyLiveData.postValue(Resource.error(error))
    }

    override fun onAddJobSuccess(job: Job) {
        _addJobLiveData.postValue(Resource.success(job))
    }

    override fun onAddJobFailure(error: Error) {
        _addJobLiveData.postValue(Resource.error(error))
    }

    override fun onAddSkillSuccess(skill: Skill) {
        _addSkillLiveData.postValue(Resource.success(skill))
    }

    override fun onAddSkillFailure(error: Error) {
        _addSkillLiveData.postValue(Resource.error(error))
    }

    override fun onAddSubjectSuccess(subject: Subject) {
        _addSubjectLiveData.postValue(Resource.success(subject))
    }

    override fun onAddSubjectFailure(error: Error) {
        _addSubjectLiveData.postValue(Resource.error(error))
    }

    override fun onGetAllSubjectsSuccess(subjects: ArrayList<Subject>) {
        _getSubjectsLiveData.postValue(Resource.success(subjects))
    }

    override fun onGetAllSubjectsFailure(error: Error) {
        _getSubjectsLiveData.postValue(Resource.error(error))
    }

    override fun onSearchSkillSuccess(skills: ArrayList<Skill>) {
        _getSkillsLiveData.postValue(Resource.success(skills))
    }

    override fun onSearchSkillFailure(error: Error) {
        _getSkillsLiveData.postValue(Resource.error(error))
    }

    override fun onGetAllCompaniesSuccess(companies: ArrayList<Company>) {
        _getCompaniesLiveData.postValue(Resource.success(companies))
    }

    override fun onGetAllCompaniesFailure(error: Error) {
        _getCompaniesLiveData.postValue(Resource.error(error))
    }

    override fun onUpdateUserSuccess(user: User) {
        _updateUserLiveData.postValue(Resource.success(user))
    }

    override fun onUpdateUserFailure(error: Error) {
        _updateUserLiveData.postValue(Resource.error(error))
    }
}