package com.example.projectsetup.ui.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.projectsetup.R
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.di.components.DaggerAddBranchActivityComponent
import com.example.projectsetup.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_add_branch.*
import javax.inject.Inject

class AddBranchActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var adminActivityViewModel: AdminActivityViewModel

    companion object {
        fun newIntent(context: Context) = Intent(context, AddBranchActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_branch)
        setWindowInsets()
        inject()
    }

    private fun setWindowInsets() {
        clHome.setOnApplyWindowInsetsListener { view, insets ->
            view.setPadding(0, insets.systemWindowInsetTop, 0, insets.systemWindowInsetBottom)
            insets
        }
    }

    private fun inject() {
        val component = DaggerAddBranchActivityComponent.builder()
            .studentHelperApplicationComponent(StudentHelper.get(this).studentHelperApplicationComponent())
            .build()
        component.injectAddBranchActivity(this)
        adminActivityViewModel =
            ViewModelProvider(this, viewModelFactory).get(AdminActivityViewModel::class.java)
    }
}