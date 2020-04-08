package com.example.projectsetup.ui.assign_project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.R
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.data.models.Status
import com.example.projectsetup.data.models.User
import com.example.projectsetup.di.components.DaggerAssignProjectActivityComponent
import com.example.projectsetup.ui.base.BaseActivity
import com.example.projectsetup.ui.dialog.DateTimeSelectionDialog
import com.example.projectsetup.ui.generic_rv.GenericDataAdapter
import com.example.projectsetup.ui.loader.ProgressModal
import com.example.projectsetup.util.CustomValidationListener
import com.example.projectsetup.util.DateTimeUtils
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.activity_assign_project.*
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

class AssignProjectActivity : BaseActivity(), DateTimeSelectionDialog.OnSubmitClickListener,
    GenericDataAdapter.OnItemDeletedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var adapter: GenericDataAdapter

    @NotEmpty
    private lateinit var etProjectTitle: MaterialEditText
    @NotEmpty
    private lateinit var etProjectDescription: MaterialEditText

    private lateinit var autoCompleteUser: AutoCompleteTextView

    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private lateinit var userText: String

    private var commaSeparatedUserIds: String = ""
    private var isUserAdded: Boolean = false
    private var isDateTimeSelected: Boolean = false
    private lateinit var validator: Validator

    private var userList: ArrayList<User> = ArrayList()
    private var users: ArrayList<String> = ArrayList()

    private lateinit var dateTimeSelectionDialog: DateTimeSelectionDialog
    private lateinit var zonedDateTime: ZonedDateTime
    private lateinit var viewModel: AssignProjectViewModel
    private lateinit var userId: String
    private lateinit var progressModal: ProgressModal

    companion object {
        private const val EXTRA_USER_ID = "EXTRA_USER_ID"
        fun newIntent(context: Context, userId: String): Intent {
            val intent = Intent(context, AssignProjectActivity::class.java)
            intent.putExtra(EXTRA_USER_ID, userId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign_project)
        setWindowInsets()
        getArguments()
        inject()
        initialiseLayout()
        setListener()
        observeData()
    }


    private fun setWindowInsets() {
        clAssignProjectContainer.setOnApplyWindowInsetsListener { view, insets ->
            view.setPadding(0, insets.systemWindowInsetTop, 0, insets.systemWindowInsetBottom)
            insets
        }
    }

    private fun getArguments() {
        intent?.let {
            it.getStringExtra(EXTRA_USER_ID)?.let { uid ->
                userId = uid
            }
        }
    }

    private fun inject() {
        val component = DaggerAssignProjectActivityComponent.builder()
            .studentHelperApplicationComponent(StudentHelper.get(this).studentHelperApplicationComponent())
            .build()

        component.injectAssignProjectActivity(this)

        progressModal = ProgressModal(this)
        validator = Validator(this)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(AssignProjectViewModel::class.java)
    }

    private fun initialiseLayout() {
        etProjectTitle = findViewById(R.id.etProjectTitle)
        etProjectDescription = findViewById(R.id.etProjectDescription)
        autoCompleteUser = findViewById(R.id.autoCompleteUser)
        rvUsers.layoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        rvUsers.adapter = adapter
        initDialog()
        viewModel.searchUser("")
    }

    private fun initDialog() {
        zonedDateTime = DateTimeUtils.getLocalDate()
        supportFragmentManager.let { fm ->
            dateTimeSelectionDialog =
                DateTimeSelectionDialog(
                    this,
                    zonedDateTime = zonedDateTime,
                    fragmentManager = fm
                )
        }

        dateTimeSelectionDialog.setOnSubmitClickListener(this)
    }

    private fun setListener() {
        btnAddProject.setOnClickListener {
            validator.validate()
        }

        tvAddDateTime.setOnClickListener {
            if (::dateTimeSelectionDialog.isInitialized) {
                dateTimeSelectionDialog.show()
            }
        }
        validator.setValidationListener(object :
            CustomValidationListener(context = this@AssignProjectActivity) {
            override fun onValidationSucceeded() {
                if (isUserAdded) {
                    if (isDateTimeSelected){
                        if (::userId.isInitialized) {
                            val projectTitle = etProjectTitle.text.toString()
                            val projectDescription = etProjectDescription.text.toString()
                            viewModel.assignProject(
                                facultyId = userId,
                                title = projectTitle,
                                description = projectDescription,
                                commaSeparatedMemberIds = commaSeparatedUserIds,
                                dayOfMonth = day,
                                month = month,
                                year = year
                            )
                        }
                    }else{
                        showErrorToast("Select date time for Project Posting.")
                    }
                } else {
                    autoCompleteUser.error = "Enter atleast one student"
                }
            }
        })

        adapter.setOnItemDeletedListener(this)

    }

    private fun observeData() {
        viewModel.searchUserLiveData.observe(this, Observer {
            when(it.status){
                Status.LOADING->{
                }
                Status.SUCCESS->{
                    it.data?.let {list->
                        this.userList = list
                        initUsersAutoComplete()
                    }
                }
                Status.ERROR->{
                    showErrorToast("${it.message}")
                }
            }
        })

        viewModel.assignProjectLiveData.observe(this, Observer {
            when(it.status){
                Status.LOADING->{
                    progressModal.show()
                }
                Status.SUCCESS->{
                    progressModal.dismiss()
                    it.data?.let {ap->
                        showSuccessToast("${ap.title} project added.")
                        finish()
                    }
                }
                Status.ERROR->{
                    progressModal.dismiss()
                    showErrorToast("${it.message}")
                }
            }
        })
    }
    private fun initUsersAutoComplete() {

        if (userList.isNotEmpty()) {
            userList.forEachIndexed { index, user ->
                users.add(user.name)
            }
        }

        val arrayAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, users)

        autoCompleteUser.setAdapter(arrayAdapter)

        autoCompleteUser.setOnTouchListener(View.OnTouchListener { v, event ->

            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= autoCompleteUser.right - autoCompleteUser.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    userText = autoCompleteUser.text.toString()
                    if (userText.isEmpty()) {
                        showErrorSnackBar("Enter valid skill", clAssignProjectContainer)
                    } else {
                        for (i in 0 until users.size) {
                            if (userText == users[i]) {
                                isUserAdded = true
                                commaSeparatedUserIds += if (commaSeparatedUserIds.isNullOrEmpty())
                                    "${userList[i].id}"
                                else
                                    ",${userList[i].id}"

                                adapter.updateGenericDataList(userText)
                                autoCompleteUser.text?.clear()
                            }
                        }
                        if (!isUserAdded)
                            showErrorSnackBar(
                                "Select skill from the options",
                                clAssignProjectContainer
                            )
                    }
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    override fun onSubmitClick(year: Int, month: Int, day: Int, hours: Int, minutes: Int) {
        this.year = year
        this.month = month
        this.day = day
        isDateTimeSelected = true
        dateTimeSelectionDialog.dismiss()
    }

    override fun onItemDeleted(position: Int) {
        adapter.onRemoveItem(position)
    }

    override fun onBackPressed() {
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left)
        super.onBackPressed()
    }
}