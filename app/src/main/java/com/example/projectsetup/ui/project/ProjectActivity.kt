package com.example.projectsetup.ui.project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.projectsetup.R
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.di.components.DaggerProjectActivityComponent
import com.example.projectsetup.ui.base.BaseActivity
import com.example.projectsetup.ui.chat.ChatActivity
import kotlinx.android.synthetic.main.acitivity_project.*
import javax.inject.Inject

class ProjectActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var userId: String

    companion object {
        private const val EXTRA_USER_ID = "EXTRA_USER_ID"
        fun newIntent(context: Context, userId: String): Intent {
            val intent = Intent(context, ProjectActivity::class.java)
            intent.putExtra(EXTRA_USER_ID, userId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_project)
        getArguments()
        inject()
        initialiseLayout()
        setListener()
        observeData()
    }

    private fun getArguments() {
        intent?.let {
            it.getStringExtra(EXTRA_USER_ID)?.let { uid ->
                userId = uid
            }
        }
    }

    private fun inject() {
        val component = DaggerProjectActivityComponent.builder()
            .studentHelperApplicationComponent(StudentHelper.get(this).studentHelperApplicationComponent())
            .build()

        component.injectProjectActivity(this)

        projectViewModel =
            ViewModelProvider(this, viewModelFactory).get(ProjectViewModel::class.java)
    }

    private fun initialiseLayout() {

    }

    private fun setListener() {
        fabChat.setOnClickListener {
            if (::userId.isInitialized){
                startActivity(ChatActivity.newIntent(this,userId))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    private fun observeData() {

    }
}