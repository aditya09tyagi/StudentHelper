package com.example.projectsetup.ui.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.projectsetup.R
import com.example.projectsetup.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_admin_home.*

class AdminHomeActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context) = Intent(context, AdminHomeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)
        setWindowInsets()
        setListeners()
    }

    private fun setWindowInsets() {
        clHome.setOnApplyWindowInsetsListener { view, insets ->
            view.setPadding(0, insets.systemWindowInsetTop, 0, insets.systemWindowInsetBottom)
            insets
        }
    }

    private fun setListeners() {
        btnAddBranch.setOnClickListener {
            startActivity(AddBranchActivity.newIntent(this))
        }
        btnAddCompany.setOnClickListener {
            startActivity(AddCompanyActivity.newIntent(this))
        }
        btnAddJob.setOnClickListener {
            startActivity(AddJobActivity.newIntent(this))
        }
        btnAddSkill.setOnClickListener {
            startActivity(AddSkillActivity.newIntent(this))
        }
        btnAddSubject.setOnClickListener {
            startActivity(AddSubjectActivity.newIntent(this))
        }
    }
}