package com.example.projectsetup.ui.project

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectsetup.R
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.data.models.Status
import com.example.projectsetup.di.components.DaggerProjectActivityComponent
import com.example.projectsetup.ui.assign_project.AssignProjectActivity
import com.example.projectsetup.ui.base.BaseActivity
import com.example.projectsetup.ui.chat.ChatActivity
import com.example.projectsetup.ui.dialog.UpdateProgressDialog
import com.example.projectsetup.ui.project.rv_project.ProjectAdapter
import com.example.projectsetup.util.Constants
import com.example.projectsetup.util.DateTimeUtils
import kotlinx.android.synthetic.main.acitivity_project.*
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class ProjectActivity : BaseActivity(), ProjectAdapter.OnItemClickListener,
    UpdateProgressDialog.OnSubmitClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var projectAdapter: ProjectAdapter

    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var userId: String
    private var userType: Int = Constants.USER_TYPE_STUDENT
    private lateinit var projectId: String
    private lateinit var updateProgressDialog: UpdateProgressDialog

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
        getDataFromViewModel()
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
        userType = sharedPreferenceUtil.getInt(Constants.EXTRA_USER_TYPE)
    }

    private fun inject() {
        val component = DaggerProjectActivityComponent.builder()
            .studentHelperApplicationComponent(StudentHelper.get(this).studentHelperApplicationComponent())
            .build()

        component.injectProjectActivity(this)
        projectViewModel =
            ViewModelProvider(this, viewModelFactory).get(ProjectViewModel::class.java)
    }

    private fun getDataFromViewModel() {
        if (userType == Constants.USER_TYPE_STUDENT) {
            if (::userId.isInitialized) {
                projectViewModel.getMyProject(userId)
                hideTeacherView()
            }
        } else {
            if (::userId.isInitialized) {
                projectViewModel.getFacultyProject(facultyId = userId)
                updateProgressDialog = UpdateProgressDialog(this)
                hideStudentView()
            }
        }
    }

    private fun hideStudentView() {
        cardViewProject.visibility = View.GONE
        fabChat.visibility = View.GONE
    }

    private fun showStudentView() {
        cardViewProject.visibility = View.VISIBLE
        fabChat.visibility = View.VISIBLE
    }

    private fun hideTeacherView() {
        rvProjects.visibility = View.GONE
        fabAddProject.visibility = View.GONE
    }

    private fun showTeacherView() {
        rvProjects.visibility = View.VISIBLE
        fabAddProject.visibility = View.VISIBLE
    }

    private fun initialiseLayout() {
        if (userType == Constants.USER_TYPE_STUDENT) {
            showStudentView()
            tvProjectName.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        } else {
            hideStudentView()
            rvProjects.layoutManager = LinearLayoutManager(this)
            rvProjects.adapter = projectAdapter
            projectAdapter.setOnItemClickListener(this)
            updateProgressDialog.setOnSubmitClickListener(this)
        }
    }

    private fun setListener() {
        fabChat.setOnClickListener {
            if (::userId.isInitialized && ::projectId.isInitialized) {
                startChatActivity(userId, projectId)
            }
        }
        fabAddProject.setOnClickListener {
            if (::userId.isInitialized)
                startActivity(AssignProjectActivity.newIntent(this, userId))
        }
    }

    private fun startChatActivity(userId: String, projectId: String) {
        startActivity(ChatActivity.newIntent(this, userId, projectId))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun observeData() {
        projectViewModel.projectLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    hideStudentView()
                    clNoProjectAssigned.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    clNoProjectAssigned.visibility = View.GONE
                    showStudentView()
                    progressBar.visibility = View.GONE
                    it.data?.let {
                        if (it.isNotEmpty()) {
                            it[0].let {
                                projectId = it.id
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
                                tvProgressValue.text = it.progress.toString()+ " %"
                                tvProjectName.text = it.title
                                clProjectContainer.visibility = View.VISIBLE
                            }
                        } else {
                            clNoProjectAssigned.visibility = View.VISIBLE
                            hideStudentView()
                            showSuccessToast("No Project Assigned Yet")
                        }
                    }
                }
                Status.ERROR -> {
                    showErrorToast("${it.message}")
                    progressBar.visibility = View.GONE
                    hideTeacherView()
                    hideStudentView()
                }
            }
        })

        projectViewModel.facultyProjectLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    hideTeacherView()
                    clNoProjectAssigned.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    clNoProjectAssigned.visibility = View.GONE
                    showTeacherView()
                    progressBar.visibility = View.GONE
                    it.data?.let { list ->
                        if (list.isNotEmpty()) {
                            projectAdapter.setProjectList(list)
                        } else {
                            clNoProjectAssigned.visibility = View.VISIBLE
                        }
                    }
                }
                Status.ERROR -> {
                    showErrorToast("${it.message}")
                    progressBar.visibility = View.GONE
                    hideTeacherView()
                }
            }
        })

        projectViewModel.updateProgressLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    it.data?.let {
                        showSuccessToast("Progress Updated")
                    }
                }
                Status.ERROR -> {
                    showErrorToast("${it.message}")
                }
            }
        })
    }

    override fun onUpdateClickListener(projectId: String, position: Int) {
        this.projectId = projectId
        updateProgressDialog.show()
    }

    override fun onChatClickListener(projectId: String, position: Int) {
        if (::userId.isInitialized) {
            this.projectId = projectId
            startChatActivity(this.projectId, userId)
        }
    }

    override fun onResume() {
        super.onResume()
        if (::userId.isInitialized)
            projectViewModel.getFacultyProject(userId)
    }

    override fun onBackPressed() {
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left)
        super.onBackPressed()
    }

    override fun onSubmitClick(updatedProgressValue: Int) {
        updateProgressDialog.dismiss()
        if (::projectId.isInitialized)
            projectViewModel.updateProjectProgress(updatedProgressValue, projectId)
    }
}