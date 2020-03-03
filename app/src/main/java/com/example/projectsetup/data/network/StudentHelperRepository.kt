package com.example.projectsetup.data.network

import com.example.projectsetup.data.models.*
import com.example.projectsetup.util.ApiCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

class StudentHelperRepository(private val studentHelperService: StudentHelperService) {

    fun login(
        name: String,
        email: String,
        avatarUrl: String? = null,
        userType: Int,
        onLoginListener: OnLoginListener
    ) {
        studentHelperService.login(name, email, avatarUrl, userType)
            .enqueue(object : Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        response.body()?.let { _ ->
                            onLoginListener.onLoginSuccess(response)
                        }
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    if (t is UnknownHostException) {
                        onLoginListener.onLoginFailure(Error(true, "Internet not connected"))
                    } else {
                        onLoginListener.onLoginFailure(Error(true, "${t.message}"))
                    }
                }
            })
    }

    fun updateUser(
        userId: String,
        age: Int,
        branch: String,
        userType: Int,
        section: String = "A",
        semester: Int,
        commaSeparatedSkillIds: String = "",
        onUpdateUserListener: OnUpdateUserListener? = null
    ) {
        studentHelperService.updateUser(
            userId,
            age,
            branch,
            userType,
            section,
            semester,
            commaSeparatedSkillIds
        ).enqueue(object : ApiCallback<User>() {
            override fun success(response: User) {
                onUpdateUserListener?.onUpdateUserSuccess(response)
            }

            override fun failure(error: Error) {
                onUpdateUserListener?.onUpdateUserFailure(error)
            }

        })
    }

    fun searchUsers(queryText: String, onSearchUserListener: OnSearchUserListener) {
        studentHelperService.searchUser(queryText)
            .enqueue(object : ApiCallback<ArrayList<User>>() {
                override fun success(response: ArrayList<User>) {
                    onSearchUserListener.onSearchUserSuccess(response)
                }

                override fun failure(error: Error) {
                    onSearchUserListener.onSearchUserFailure(error)
                }

            })
    }

    fun searchSkills(queryText: String, onSearchSkillListener: OnSearchSkillListener) {
        studentHelperService.searchSkill(queryText)
            .enqueue(object : ApiCallback<ArrayList<Skill>>() {
                override fun success(response: ArrayList<Skill>) {
                    onSearchSkillListener.onSearchSkillSuccess(response)
                }

                override fun failure(error: Error) {
                    onSearchSkillListener.onSearchSkillFailure(error)
                }

            })
    }

    fun getPastCompanies(userId: String, onPastCompaniesListener: OnPastCompaniesListener) {
        studentHelperService.getPastCompanies(userId)
            .enqueue(object : ApiCallback<ArrayList<Past>>() {
                override fun success(response: ArrayList<Past>) {
                    onPastCompaniesListener.onPastCompanySuccess(response)
                }

                override fun failure(error: Error) {
                    onPastCompaniesListener.onPastCompanyFailure(error)
                }
            })
    }

    fun getUpcomingCompanies(
        userId: String,
        onUpcomingCompaniesListener: OnUpcomingCompaniesListener
    ) {
        studentHelperService.getUpcomingCompanies(userId)
            .enqueue(object : ApiCallback<ArrayList<Upcoming>>() {
                override fun success(response: ArrayList<Upcoming>) {
                    onUpcomingCompaniesListener.onUpcomingCompanySuccess(response)
                }

                override fun failure(error: Error) {
                    onUpcomingCompaniesListener.onUpcomingCompanyFailure(error)
                }
            })
    }

    fun getLiveCompanies(userId: String, onGetLiveCompaniesListener: OnGetLiveCompaniesListener) {
        studentHelperService.getLiveCompanies(userId)
            .enqueue(object : ApiCallback<ArrayList<Live>>() {
                override fun success(response: ArrayList<Live>) {
                    onGetLiveCompaniesListener.onGetLiveCompaniesSuccess(response)
                }

                override fun failure(error: Error) {
                    onGetLiveCompaniesListener.onGetLiveCompaniesFailure(error)
                }
            })
    }

    fun getAllCompanies(onGetAllCompaniesListener: OnGetAllCompaniesListener) {
        studentHelperService.getAllCompanies().enqueue(object : ApiCallback<ArrayList<Company>>() {
            override fun success(response: ArrayList<Company>) {
                onGetAllCompaniesListener.onGetAllCompaniesSuccess(response)
            }

            override fun failure(error: Error) {
                onGetAllCompaniesListener.onGetAllCompaniesFailure(error)
            }
        })
    }

    fun getCompanyById(companyId: String, onGetCompanyByIdListener: OnGetCompanyByIdListener) {
        studentHelperService.getCompanyById(companyId).enqueue(object : ApiCallback<Company>() {
            override fun success(response: Company) {
                onGetCompanyByIdListener.onGetCompanyByIdSuccess(response)
            }

            override fun failure(error: Error) {
                onGetCompanyByIdListener.onGetCompanyByIdFailure(error)
            }
        })
    }

    fun getAllBranches(onGetAllBranchesListener: OnGetAllBranchesListener) {
        studentHelperService.getAllBranches().enqueue(object : ApiCallback<ArrayList<Branch>>() {
            override fun success(response: ArrayList<Branch>) {
                onGetAllBranchesListener.onGetAllBranchesSuccess(response)
            }

            override fun failure(error: Error) {
                onGetAllBranchesListener.onGetAllBranchesFailure(error)
            }
        })
    }

    fun getAllSubjects(onGetAllSubjectsListener: OnGetAllSubjectsListener) {
        studentHelperService.getAllSubjects().enqueue(object : ApiCallback<ArrayList<Subject>>() {
            override fun success(response: ArrayList<Subject>) {
                onGetAllSubjectsListener.onGetAllSubjectsSuccess(response)
            }

            override fun failure(error: Error) {
                onGetAllSubjectsListener.onGetAllSubjectsFailure(error)
            }
        })
    }

    fun getAllSkills(onGetAllSkillsListener: OnGetAllSkillsListener) {
        studentHelperService.getAllSkills().enqueue(object : ApiCallback<ArrayList<Skill>>() {
            override fun success(response: ArrayList<Skill>) {
                onGetAllSkillsListener.onGetAllSkillsSuccess(response)
            }

            override fun failure(error: Error) {
                onGetAllSkillsListener.onGetAllSkillsFailure(error)
            }
        })
    }

    fun getMyProject(userId: String, onGetMyProjectListener: OnGetMyProjectListener) {
        studentHelperService.getMyProject(userId).enqueue(object : ApiCallback<ArrayList<Project>>() {
            override fun success(response: ArrayList<Project>) {
                onGetMyProjectListener.onGetMyProjectSuccess(response)
            }

            override fun failure(error: Error) {
                onGetMyProjectListener.onGetMyProjectFailure(error)
            }
        })
    }

    fun getProjectUnderFaculty(
        facultyId: String,
        onGetProjectUnderFacultyListener: OnGetProjectUnderFacultyListener
    ) {
        studentHelperService.getProjectUnderFaculty(facultyId)
            .enqueue(object : ApiCallback<ArrayList<Project>>() {
                override fun success(response: ArrayList<Project>) {
                    onGetProjectUnderFacultyListener.onGetProjectUnderFacultySuccess(response)
                }

                override fun failure(error: Error) {
                    onGetProjectUnderFacultyListener.onGetProjectUnderFacultyFailure(error)
                }
            })
    }

    fun applyForJob(
        userId: String,
        jobId: String,
        onApplyForJobListener: OnApplyForJobListener
    ) {
        studentHelperService.applyForJob(userId, jobId)
            .enqueue(object : ApiCallback<JobApply>() {
                override fun success(response: JobApply) {
                    onApplyForJobListener.onApplyForJobSuccess(response)
                }

                override fun failure(error: Error) {
                    onApplyForJobListener.onApplyForJobFailure(error)
                }
            })
    }

    fun updateProjectProgress(
        updateProgress: Int,
        projectId: String,
        onUpdateProjectProgressListener: OnUpdateProjectProgressListener
    ) {
        studentHelperService.updateProjectProgress(updateProgress, projectId)
            .enqueue(object : ApiCallback<UpdateProject>() {
                override fun success(response: UpdateProject) {
                    onUpdateProjectProgressListener.onUpdateProjectProgressSuccess(response)
                }

                override fun failure(error: Error) {
                    onUpdateProjectProgressListener.onUpdateProjectProgressFailure(error)
                }
            })
    }

    fun addBranch(
        branchName: String,
        commaSeparatedSubjectIds: String,
        userId: String,
        onAddBranchListener: OnAddBranchListener
    ) {
        studentHelperService.addBranch(branchName, commaSeparatedSubjectIds, userId)
            .enqueue(object : ApiCallback<Branch>() {
                override fun success(response: Branch) {
                    onAddBranchListener.onAddBranchSuccess(response)
                }

                override fun failure(error: Error) {
                    onAddBranchListener.onAddBranchFailure(error)
                }

            })
    }

    fun addCompany(
        companyName: String,
        websiteLink: String,
        companyLogo: String,
        userId: String,
        onAddCompanyListener: OnAddCompanyListener
    ) {
        studentHelperService.addCompany(companyName, websiteLink, companyLogo, userId)
            .enqueue(object : ApiCallback<Company>() {
                override fun success(response: Company) {
                    onAddCompanyListener.onAddCompanySuccess(response)
                }

                override fun failure(error: Error) {
                    onAddCompanyListener.onAddCompanyFailure(error)
                }

            })
    }

    fun addJob(
        companyId: String,
        jobTitle: String,
        jobDescription: String,
        driveLocation: String,
        commaSeparatedSkillIds: String,
        driveDate: Int,
        driveMonth: Int,
        driveYear: Int,
        driveHour: Int,
        driveMinute: Int,
        userId: String,
        onAddJobListener: OnAddJobListener
    ) {
        studentHelperService.addJob(
            companyId,
            jobTitle,
            jobDescription,
            driveLocation,
            commaSeparatedSkillIds,
            driveDate,
            driveMonth,
            driveYear,
            driveHour,
            driveMinute,
            userId
        )
            .enqueue(object : ApiCallback<Job>() {
                override fun success(response: Job) {
                    onAddJobListener.onAddJobSuccess(response)
                }

                override fun failure(error: Error) {
                    onAddJobListener.onAddJobFailure(error)
                }

            })
    }

    fun addSubject(
        subjectName: String,
        userId: String,
        onAddSubjectsListener: OnAddSubjectListener
    ) {
        studentHelperService.addSubject(subjectName, userId)
            .enqueue(object : ApiCallback<Subject>() {
                override fun success(response: Subject) {
                    onAddSubjectsListener.onAddSubjectSuccess(response)
                }

                override fun failure(error: Error) {
                    onAddSubjectsListener.onAddSubjectFailure(error)
                }

            })
    }

    fun addSkill(
        skillName: String,
        userId: String,
        onAddSkillListener: OnAddSkillListener
    ) {
        studentHelperService.addSkill(skillName, userId)
            .enqueue(object : ApiCallback<Skill>() {
                override fun success(response: Skill) {
                    onAddSkillListener.onAddSkillSuccess(response)
                }

                override fun failure(error: Error) {
                    onAddSkillListener.onAddSkillFailure(error)
                }

            })
    }

    fun assignProject(
        facultyId: String,
        title: String,
        description:String,
        commaSeparatedMemberIds: String,
        dayOfMonth:Int,
        month:Int,
        year:Int,
        onAssignProjectListener: OnAssignProjectListener
    ) {
        studentHelperService.assignProject(facultyId,title,description,commaSeparatedMemberIds,dayOfMonth, month, year)
            .enqueue(object : ApiCallback<AssignProject>() {
                override fun success(response: AssignProject) {
                    onAssignProjectListener.onAssignProjectSuccess(response)
                }

                override fun failure(error: Error) {
                    onAssignProjectListener.onAssignProjectFailure(error)
                }

            })
    }

    //-------INTERFACES----------


    interface OnLoginListener {

        fun onLoginSuccess(response: Response<User>)

        fun onLoginFailure(error: Error)

    }

    interface OnUpdateUserListener {

        fun onUpdateUserSuccess(user: User)

        fun onUpdateUserFailure(error: Error)

    }

    interface OnSearchUserListener {

        fun onSearchUserSuccess(userList: ArrayList<User>)

        fun onSearchUserFailure(error: Error)

    }

    interface OnSearchSkillListener {

        fun onSearchSkillSuccess(skills: ArrayList<Skill>)

        fun onSearchSkillFailure(error: Error)

    }

    interface OnPastCompaniesListener {

        fun onPastCompanySuccess(past: ArrayList<Past>)

        fun onPastCompanyFailure(error: Error)

    }

    interface OnUpcomingCompaniesListener {

        fun onUpcomingCompanySuccess(upcoming: ArrayList<Upcoming>)

        fun onUpcomingCompanyFailure(error: Error)

    }

    interface OnGetLiveCompaniesListener {

        fun onGetLiveCompaniesSuccess(liveCompanies: ArrayList<Live>)

        fun onGetLiveCompaniesFailure(error: Error)

    }

    interface OnGetAllCompaniesListener {

        fun onGetAllCompaniesSuccess(companies: ArrayList<Company>)

        fun onGetAllCompaniesFailure(error: Error)

    }


    interface OnGetCompanyByIdListener {

        fun onGetCompanyByIdSuccess(company: Company)

        fun onGetCompanyByIdFailure(error: Error)

    }

    interface OnGetAllBranchesListener {

        fun onGetAllBranchesSuccess(branches: ArrayList<Branch>)

        fun onGetAllBranchesFailure(error: Error)

    }

    interface OnGetAllSubjectsListener {

        fun onGetAllSubjectsSuccess(subjects: ArrayList<Subject>)

        fun onGetAllSubjectsFailure(error: Error)

    }

    interface OnGetAllSkillsListener {

        fun onGetAllSkillsSuccess(skills: ArrayList<Skill>)

        fun onGetAllSkillsFailure(error: Error)

    }

    interface OnGetMyProjectListener {

        fun onGetMyProjectSuccess(project: ArrayList<Project>)

        fun onGetMyProjectFailure(error: Error)

    }

    interface OnGetProjectUnderFacultyListener {

        fun onGetProjectUnderFacultySuccess(project: ArrayList<Project>)

        fun onGetProjectUnderFacultyFailure(error: Error)

    }

    interface OnApplyForJobListener {

        fun onApplyForJobSuccess(jobApply: JobApply)

        fun onApplyForJobFailure(error: Error)

    }

    interface OnUpdateProjectProgressListener {

        fun onUpdateProjectProgressSuccess(updatedProject:UpdateProject)

        fun onUpdateProjectProgressFailure(error: Error)

    }

    interface OnAddBranchListener {

        fun onAddBranchSuccess(branch: Branch)

        fun onAddBranchFailure(error: Error)

    }

    interface OnAddSubjectListener {

        fun onAddSubjectSuccess(subject: Subject)

        fun onAddSubjectFailure(error: Error)

    }

    interface OnAddSkillListener {

        fun onAddSkillSuccess(skill: Skill)

        fun onAddSkillFailure(error: Error)

    }

    interface OnAddCompanyListener {

        fun onAddCompanySuccess(company: Company)

        fun onAddCompanyFailure(error: Error)

    }

    interface OnAddJobListener {

        fun onAddJobSuccess(job: Job)

        fun onAddJobFailure(error: Error)

    }

    interface OnAssignProjectListener {

        fun onAssignProjectSuccess(project: AssignProject)

        fun onAssignProjectFailure(error: Error)

    }

}