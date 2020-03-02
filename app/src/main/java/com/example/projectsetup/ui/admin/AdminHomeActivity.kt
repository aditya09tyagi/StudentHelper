package com.example.projectsetup.ui.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.projectsetup.R
import com.example.projectsetup.ui.base.BaseActivity
import com.example.projectsetup.util.Constants
import kotlinx.android.synthetic.main.activity_admin_home.*

class AdminHomeActivity : BaseActivity() {

    private lateinit var userId: String

    companion object {
        fun newIntent(context: Context) = Intent(context, AdminHomeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)
        getUserId()
        setWindowInsets()
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
    }
}