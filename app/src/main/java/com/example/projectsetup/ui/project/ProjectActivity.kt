package com.example.projectsetup.ui.project

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.projectsetup.R
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.data.models.Status
import com.example.projectsetup.di.components.DaggerProjectActivityComponent
import com.example.projectsetup.ui.base.BaseActivity
import com.example.projectsetup.ui.chat.ChatActivity
import com.example.projectsetup.util.DateTimeUtils
import kotlinx.android.synthetic.main.acitivity_project.*
import org.threeten.bp.format.DateTimeFormatter
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
        getProject()
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

    private fun getProject() {
        if (::userId.isInitialized) {
            projectViewModel.getMyProject(userId)
        }
    }

    private fun initialiseLayout() {
        tvProjectName.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }

    private fun setListener() {
        fabChat.setOnClickListener {
            if (::userId.isInitialized) {
                startActivity(ChatActivity.newIntent(this, userId))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    private fun observeData() {
        projectViewModel.projectLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    clProjectContainer.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.let {
                        if (it.isNotEmpty()) {
                            it[0].let {
                                var members = ""
                                it.userMembers.forEachIndexed { index, user ->
                                    if (index == it.userMembers.size - 1) {
                                        members += user.name
                                    } else {
                                        members = members + user.name + "\n"
                                    }
                                }
                                tvMembers.text = members
                                tvCoordinator.text = it.userFaculty.name
                                tvDeadline.text =
                                    DateTimeUtils.getLocalDateFromString(it.deadline)
                                        .format(
                                            DateTimeFormatter.BASIC_ISO_DATE
                                        )
                                projectProgress.progress = it.progress
                                tvProgressValue.text = it.progress.toString()
                                tvProjectName.text = it.title
                                clProjectContainer.visibility = View.VISIBLE
                            }
                        } else {
                            showSuccessToast("No Project Assigned Yet")
                        }
                    }
                }
                Status.ERROR -> {
                    showErrorToast("${it.message}")
                    progressBar.visibility = View.GONE
                    clProjectContainer.visibility = View.GONE
                }
            }
        })
    }
}