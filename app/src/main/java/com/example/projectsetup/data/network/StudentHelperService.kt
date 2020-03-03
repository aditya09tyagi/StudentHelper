package com.example.projectsetup.data.network

import com.example.projectsetup.data.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface StudentHelperService {

    @GET("/v1/user/login")
    fun login(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("avatar_url") avatarUrl: String?,
        @Query("userType") userType: Int
    ): Call<User>

    @POST("v1/home/skill")
    fun addSkill(
        @Query("skill_name") skillName: String,
        @Query("userId") userId: String
    ): Call<Skill>

    @POST("v1/home/branch")
    fun addBranch(
        @Query("branch_name") branchName: String,
        @Query("subjects") commaSeparatedSubjectIds: String,
        @Query("userId") userId: String
    ): Call<Branch>

    @POST("v1/placement/company")
    fun addCompany(
        @Query("name") companyName: String,
        @Query("website") websiteLink: String,
        @Query("logo") companyLogo: String,
        @Query("userId") userId: String
    ): Call<Company>

    @POST("v1/placement/job")
    fun addJob(
        @Query("company") companyName: String,
        @Query("title") companyTitle: String,
        @Query("description") companyDescription: String,
        @Query("place") driveLocation: String,
        @Query("skills") commaSeparatedSkillIds: String,
        @Query("date") driveDate: Int,
        @Query("month") driveMonth: Int,
        @Query("year") driveYear: Int,
        @Query("hour") driveHour: Int,
        @Query("minute") driveMinute: Int,
        @Query("userId") userId: String
    ): Call<Job>

    @POST("v1/home/subject")
    fun addSubject(
        @Query("subject_name") subjectName: String,
        @Query("userId") userId: String
    ): Call<Subject>

    @GET("v1/home/skill")
    fun getAllSkills(): Call<ArrayList<Skill>>

    @GET("v1/home/subject")
    fun getAllSubjects(): Call<ArrayList<Subject>>

    @GET("v1/home/branch")
    fun getAllBranches(): Call<ArrayList<Branch>>

    @GET("v1/home/searchSkill")
    fun searchSkill(
        @Query("text") queryText: String
    ): Call<ArrayList<Skill>>

    @GET("v1/user/updateUser")
    fun updateUser(
        @Query("userId") userId: String,
        @Query("age") age: Int,
        @Query("branch") branch: String,
        @Query("userType") userType: Int,
        @Query("section") section: String,
        @Query("semester") semester: Int,
        @Query("skills") commaSeparatedSkillIds: String
    ): Call<User>

    @GET("v1/placement/company")
    fun getAllCompanies(): Call<ArrayList<Company>>

    @GET("placement/schedule/live")
    fun getLiveCompanies(
        @Query("userId") userId: String
    ): Call<ArrayList<Live>>

    @GET("/placement/schedule/past")
    fun getPastCompanies(
        @Query("userId") userId: String
    ): Call<ArrayList<Past>>

    @GET("/placement/schedule/upcoming")
    fun getUpcomingCompanies(
        @Query("userId") userId: String
    ): Call<ArrayList<Upcoming>>

    @POST("/placement/job/apply")
    fun applyForJob(
        @Query("userId") userId: String,
        @Query("job") jobId: String
    ): Call<JobApply>

    @GET("/home/company")
    fun getCompanyById(
        @Query("company") companyId: String
    ): Call<Company>

    @GET("/project/getMyProject")
    fun getMyProject(
        @Query("user") userId: String
    ): Call<ArrayList<Project>>

    @POST("/project/assignproject")
    fun assignProject(
        @Query("faculty") facultyId: String,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("members") commaSeparatedMembersIds: String,
        @Query("date") dayOfMonth: String,
        @Query("month") month: String,
        @Query("year") year: String
    )

    @POST("/project/updateProgress")
    fun updateProjectProgress(
        @Query("progress") updatedProgress: Int,
        @Query("project") projectId: String
    ): Call<Project>

    @GET("/project/getProjectUnderFaculty")
    fun getProjectUnderFaculty(
        @Query("faculty") facultyId: String
    ): Call<ArrayList<Project>>
}