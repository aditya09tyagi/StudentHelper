package com.example.projectsetup.ui.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.projectsetup.R
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.di.components.DaggerAdminHomeActivityComponent
import com.example.projectsetup.ui.base.BaseActivity
import com.example.projectsetup.ui.project.ProjectActivity
import com.example.projectsetup.util.Constants
import kotlinx.android.synthetic.main.activity_admin_home.*
import javax.inject.Inject

class AdminHomeActivity : BaseActivity() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var userId: String
    private lateinit var adminActivityViewModel: AdminActivityViewModel

    companion object {
        fun newIntent(context: Context) = Intent(context, AdminHomeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)
        setWindowInsets()
        getUserId()
        inject()
        setPlayerIdForAdminTypeUser()
        setListeners()
    }

    private fun setWindowInsets() {
        clHome.setOnApplyWindowInsetsListener { view, insets ->
            view.setPadding(0, insets.systemWindowInsetTop, 0, insets.systemWindowInsetBottom)
            insets
        }
    }

    private fun getUserId() {
        userId = sharedPreferenceUtil.getString(Constants.EXTRA_USER_ID)
    }


    private fun inject() {
        val component = DaggerAdminHomeActivityComponent.builder()
            .studentHelperApplicationComponent(
                StudentHelper.get(this).studentHelperApplicationComponent()
            )
            .build()
        component.injectAdminActivity(this)

        adminActivityViewModel =
            ViewModelProvider(this, viewModelFactory).get(AdminActivityViewModel::class.java)
    }

    private fun setPlayerIdForAdminTypeUser() {
        if (::userId.isInitialized) {
            adminActivityViewModel.updateUser(userId, Constants.USER_TYPE_ADMIN)
        }
    }

    private fun setListeners() {
        btnAddBranch.setOnClickListener {
            if (::userId.isInitialized)
                startActivity(AddBranchActivity.newIntent(this, userId))
        }
        btnAddCompany.setOnClickListener {
            if (::userId.isInitialized)
                startActivity(AddCompanyActivity.newIntent(this, userId))
        }
        btnAddJob.setOnClickListener {
            if (::userId.isInitialized)
                startActivity(AddJobActivity.newIntent(this, userId))
        }
        btnAddSkill.setOnClickListener {
            if (::userId.isInitialized)
                startActivity(AddSkillActivity.newIntent(this, userId))
        }
        btnAddSubject.setOnClickListener {
            if (::userId.isInitialized)
                startActivity(AddSubjectActivity.newIntent(this, userId))
        }

        btnProjects.setOnClickListener {
            if (::userId.isInitialized)
                startActivity(ProjectActivity.newIntent(this, userId))
        }
    }
}