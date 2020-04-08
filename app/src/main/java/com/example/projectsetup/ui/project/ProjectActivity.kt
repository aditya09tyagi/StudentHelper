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
import com.example.projectsetup.ui.dialog.NotificationSenderDialog
import com.example.projectsetup.ui.dialog.UpdateProgressDialog
import com.example.projectsetup.ui.project.rv_project.ProjectAdapter
import com.example.projectsetup.util.Constants
import com.example.projectsetup.util.DateTimeUtils
import kotlinx.android.synthetic.main.acitivity_project.*
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class ProjectActivity : BaseActivity(),
    ProjectAdapter.OnItemClickListener,
    UpdateProgressDialog.OnSubmitClickListener,
    NotificationSenderDialog.OnSubmitNotificationClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var projectAdapter: ProjectAdapter

    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var userId: String
    private var userType: Int = Constants.USER_TYPE_STUDENT
    private lateinit var projectId: String
    private lateinit var updateProgressDialog: UpdateProgressDialog
    private lateinit var sendNotificationDialog: NotificationSenderDialog

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
        getDataFromViewModel()
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
            .studentHelperApplicationComponent(
                StudentHelper.get(this).studentHelperApplicationComponent()
            )
            .build()

        component.injectProjectActivity(this)
        projectViewModel =
            ViewModelProvider(this, viewModelFactory).get(ProjectViewModel::class.java)
    }

    private fun initialiseLayout() {
        if (userType == Constants.USER_TYPE_STUDENT) {
            showStudentView()
            hideTeacherOrAdminView()
            tvProjectName.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        } else {
            hideStudentView()
            showTeacherOrAdminView()
            rvProjects.layoutManager = LinearLayoutManager(this)
            rvProjects.adapter = projectAdapter
            projectAdapter.setOnItemClickListener(this)
        }
    }

    private fun getDataFromViewModel() {
        if (::userId.isInitialized) {
            when (userType) {
                Constants.USER_TYPE_STUDENT -> {
                    projectViewModel.getMyProject(userId)
                }
                Constants.USER_TYPE_FACULTY -> {
                    projectViewModel.getFacultyProject(facultyId = userId)
                    updateProgressDialog = UpdateProgressDialog(this)
                }
                Constants.USER_TYPE_ADMIN -> {
                    projectViewModel.getAllProjects()
                    sendNotificationDialog = NotificationSenderDialog(this)
                }
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

    private fun hideTeacherOrAdminView() {
        rvProjects.visibility = View.GONE
        fabAddProjectOrSendNotification.visibility = View.GONE
    }

    private fun showTeacherOrAdminView() {
        rvProjects.visibility = View.VISIBLE
        if (userType == Constants.USER_TYPE_FACULTY) {
            fabAddProjectOrSendNotification.setImageResource(R.drawable.ic_add)
        } else if (userType == Constants.USER_TYPE_ADMIN) {
            fabAddProjectOrSendNotification.setImageResource(R.drawable.ic_notification)
        }
        fabAddProjectOrSendNotification.visibility = View.VISIBLE
    }

    private fun setListener() {
        fabChat.setOnClickListener {
            if (::userId.isInitialized && ::projectId.isInitialized) {
                startChatActivity(userId, projectId)
            }
        }
        fabAddProjectOrSendNotification.setOnClickListener {
            if (::userId.isInitialized) {
                if (userType == Constants.USER_TYPE_FACULTY) {
                    updateProgressDialog.setOnSubmitClickListener(this)
                    startActivity(AssignProjectActivity.newIntent(this, userId))
                } else if (userType == Constants.USER_TYPE_ADMIN) {
                    sendNotificationDialog.show()
                    sendNotificationDialog.setOnSubmitNotificationClickListener(this)
                }
            }
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
                                tvProgressValue.text = it.progress.toString() + " %"
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
                    hideTeacherOrAdminView()
                    hideStudentView()
                }
            }
        })

        projectViewModel.facultyProjectLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    hideTeacherOrAdminView()
                    clNoProjectAssigned.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    clNoProjectAssigned.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    it.data?.let { list ->
                        if (list.isNotEmpty()) {
                            projectAdapter.setProjectList(list,userType)
                        } else {
                            clNoProjectAssigned.visibility = View.VISIBLE
                        }
                    }
                }
                Status.ERROR -> {
                    showErrorToast("${it.message}")
                    progressBar.visibility = View.GONE
                    hideTeacherOrAdminView()
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

        projectViewModel.sendNotificationToAllLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    it.data?.let {
                        showSuccessToast("Notification Sent Successfully.")
                    }
                }
                Status.ERROR -> {
                    showErrorToast("${it.message}")
                }
            }
        })

        projectViewModel.getAllProjectsLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    hideTeacherOrAdminView()
                    clNoProjectAssigned.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    clNoProjectAssigned.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    it.data?.let { list ->
                        if (list.isNotEmpty()) {
                            projectAdapter.setProjectList(list,userType)
                        } else {
                            clNoProjectAssigned.visibility = View.VISIBLE
                        }
                    }
                }
                Status.ERROR -> {
                    showErrorToast("${it.message}")
                    progressBar.visibility = View.GONE
                    hideTeacherOrAdminView()
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

    override fun onSubmitNotificationClick(
        notificationTitle: String,
        notificationDescription: String
    ) {
        sendNotificationDialog.dismiss()
        projectViewModel.sendNotificationToAll(notificationTitle, notificationDescription)
    }
}