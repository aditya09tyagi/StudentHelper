package com.example.projectsetup.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectsetup.data.models.*
import com.example.projectsetup.data.network.StudentHelperRepository
import javax.inject.Inject

class AdminActivityViewModel @Inject constructor(
    private val studentHelperRepository: StudentHelperRepository
) : ViewModel(),
    StudentHelperRepository.OnAddBranchListener,
    StudentHelperRepository.OnAddCompanyListener,
    StudentHelperRepository.OnAddJobListener,
    StudentHelperRepository.OnAddSkillListener,
    StudentHelperRepository.OnAddSubjectListener {

    private val _branchLiveData = MutableLiveData<Resource<Branch>>()
    val branchLiveData: LiveData<Resource<Branch>>
        get() = _branchLiveData

    private val _companyLiveData = MutableLiveData<Resource<Company>>()
    val companyLiveData: LiveData<Resource<Company>>
        get() = _companyLiveData

    private val _jobLiveData = MutableLiveData<Resource<Job>>()
    val jobLiveData: LiveData<Resource<Job>>
        get() = _jobLiveData

    private val _skillLiveData = MutableLiveData<Resource<Skill>>()
    val skillLiveData: LiveData<Resource<Skill>>
        get() = _skillLiveData

    private val _subjectLiveData = MutableLiveData<Resource<Subject>>()
    val subjectLiveData: LiveData<Resource<Subject>>
        get() = _subjectLiveData

    fun addBranch(
        branchName: String,
        commaSeparatedSubjectIds: String,
        userId: String
    ) {
        _branchLiveData.postValue(Resource.loading())
        studentHelperRepository.addBranch(branchName, commaSeparatedSubjectIds, userId, this)
    }

    fun addCompany(
        companyName: String,
        websiteLink: String,
        companyLogo: String,
        userId: String
    ) {
        _companyLiveData.postValue(Resource.loading())
        studentHelperRepository.addCompany(companyName, websiteLink, companyLogo, userId, this)
    }

    fun addJob(
        companyName: String,
        companyTitle: String,
        companyDescription: String,
        driveLocation: String,
        commaSeparatedSkillIds: String,
        driveDate: String,
        driveMonth: String,
        driveYear: String,
        driveHour: String,
        driveMinute: String,
        userId: String
    ) {
        _jobLiveData.postValue(Resource.loading())
        studentHelperRepository.addJob(
            companyName,
            companyTitle,
            companyDescription,
            driveLocation,
            commaSeparatedSkillIds,
            driveDate,
            driveMonth,
            driveYear,
            driveHour,
            driveMinute,
            userId,
            this
        )
    }

    fun addSkill(skillName: String, userId: String) {
        _skillLiveData.postValue(Resource.loading())
        studentHelperRepository.addSkill(skillName, userId, this)
    }

    fun addSubject(subjectName: String, userID: String) {
        _subjectLiveData.postValue(Resource.loading())
        studentHelperRepository.addSubject(subjectName, userID, this)
    }

    override fun onAddBranchSuccess(branch: Branch) {
        _branchLiveData.postValue(Resource.success(branch))
    }

    override fun onAddBranchFailure(error: Error) {
        _branchLiveData.postValue(Resource.error(error))
    }

    override fun onAddCompanySuccess(company: Company) {
        _companyLiveData.postValue(Resource.success(company))
    }

    override fun onAddCompanyFailure(error: Error) {
        _companyLiveData.postValue(Resource.error(error))
    }

    override fun onAddJobSuccess(job: Job) {
        _jobLiveData.postValue(Resource.success(job))
    }

    override fun onAddJobFailure(error: Error) {
        _jobLiveData.postValue(Resource.error(error))
    }

    override fun onAddSkillSuccess(skill: Skill) {
        _skillLiveData.postValue(Resource.success(skill))
    }

    override fun onAddSkillFailure(error: Error) {
        _skillLiveData.postValue(Resource.error(error))
    }

    override fun onAddSubjectSuccess(subject: Subject) {
        _subjectLiveData.postValue(Resource.success(subject))
    }

    override fun onAddSubjectFailure(error: Error) {
        _subjectLiveData.postValue(Resource.error(error))
    }
}