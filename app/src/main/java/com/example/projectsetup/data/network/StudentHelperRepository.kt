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
        onLoginListener: OnLoginListener
    ) {
        studentHelperService.login(name, email, avatarUrl).enqueue(object : Callback<User> {

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
        age: String = "",
        branch: String = "",
        userType: String= "",
        section: String = "",
        semester: String = "",
        commaSeparatedSkillIds: String = "",
        onUpdateUserListener: OnUpdateUserListener? = null
    ){
        studentHelperService.updateUser(userId,age,branch,userType,section,semester,commaSeparatedSkillIds).enqueue(object :ApiCallback<User>(){
            override fun success(response: User) {
                onUpdateUserListener?.onUpdateUserSuccess(response)
            }

            override fun failure(error: Error) {
                onUpdateUserListener?.onUpdateUserFailure(error)
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

    fun upcomingCompanies(userId: String, onUpcomingCompaniesListener: OnUpcomingCompaniesListener){
        studentHelperService.upcomingCompanies(userId).enqueue(object :ApiCallback<ArrayList<Upcoming>>(){
            override fun success(response: ArrayList<Upcoming>) {
                onUpcomingCompaniesListener.onUpcomingCompanySuccess(response)
            }

            override fun failure(error: Error) {
                onUpcomingCompaniesListener.onUpcomingCompanyFailure(error)
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

    interface OnSearchSkillListener {

        fun onSearchSkillSuccess(skills: ArrayList<Skill>)

        fun onSearchSkillFailure(error: Error)

    }

    interface OnUpcomingCompaniesListener {

        fun onUpcomingCompanySuccess(upcoming: ArrayList<Upcoming>)

        fun onUpcomingCompanyFailure(error: Error)

    }
}