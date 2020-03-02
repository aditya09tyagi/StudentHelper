package com.example.projectsetup.ui.admin

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
import com.example.projectsetup.data.models.Subject
import com.example.projectsetup.di.components.DaggerAddBranchActivityComponent
import com.example.projectsetup.ui.base.BaseActivity
import com.example.projectsetup.ui.generic_rv.GenericDataAdapter
import com.example.projectsetup.ui.loader.ProgressModal
import kotlinx.android.synthetic.main.activity_add_branch.*
import javax.inject.Inject

class AddBranchActivity : BaseActivity(), GenericDataAdapter.OnItemDeletedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var adapter: GenericDataAdapter

    private lateinit var adminActivityViewModel: AdminActivityViewModel
    private var subjectsList: ArrayList<Subject> = ArrayList()
    private var subjects: ArrayList<String> = ArrayList()
    private lateinit var userId: String
    private var subjectText: String = ""
    private var isSubjectAdded: Boolean = false
    private var commaSeparatedSubjectIds: String = ""
    private lateinit var progressModal: ProgressModal
    private lateinit var autoCompleteSubjects: AutoCompleteTextView

    companion object {
        private const val EXTRA_USER_ID = "EXTRA_USER_ID"
        fun newIntent(context: Context, userId: String): Intent {
            val intent = Intent(context, AddBranchActivity::class.java)
            intent.putExtra(EXTRA_USER_ID, userId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_branch)
        setWindowInsets()
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

    private fun setWindowInsets() {
        clAddBranch.setOnApplyWindowInsetsListener { view, insets ->
            view.setPadding(0, insets.systemWindowInsetTop, 0, insets.systemWindowInsetBottom)
            insets
        }
    }

    private fun inject() {
        val component = DaggerAddBranchActivityComponent.builder()
            .studentHelperApplicationComponent(StudentHelper.get(this).studentHelperApplicationComponent())
            .build()
        component.injectAddBranchActivity(this)

        progressModal = ProgressModal(this)

        adminActivityViewModel =
            ViewModelProvider(this, viewModelFactory).get(AdminActivityViewModel::class.java)
    }

    private fun initialiseLayout() {
        autoCompleteSubjects = findViewById(R.id.autoCompleteSubjects)
        adminActivityViewModel.getSubjects()

        rvSubjects.layoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        rvSubjects.adapter = adapter

    }

    private fun setListener() {
        btnAddBranch.setOnClickListener {
            if (isSubjectAdded) {
                if (::userId.isInitialized) {
                    val branch = etBranch.text.toString()
                    adminActivityViewModel.addBranch(
                        branchName = branch,
                        commaSeparatedSubjectIds = commaSeparatedSubjectIds,
                        userId = userId
                    )
                }
            } else {
                autoCompleteSubjects.error = "Enter atleast one subject"
            }
        }

        adapter.setOnItemDeletedListener(this)

    }

    private fun observeData() {
        adminActivityViewModel.getSubjectLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    it.data?.let {
                        subjectsList = it
                        initAutoCompleteView()
                    }
                }
                Status.ERROR -> {
                }
            }
        })
        adminActivityViewModel.addBranchLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressModal.show()
                }
                Status.SUCCESS -> {
                    progressModal.dismiss()
                    it.data?.let {
                        showSuccessToast("${it.branchName} added")
                    }
                }
                Status.ERROR -> {
                    progressModal.dismiss()
                    it.message?.let {
                        showErrorToast(it)
                    }
                }
            }
        })
    }

    private fun initAutoCompleteView() {
        if (subjectsList.isNotEmpty()) {
            subjectsList.forEachIndexed { index, subject ->
                subjects.add(subject.name)
            }
        }


        val arrayAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, subjects)

        autoCompleteSubjects.setAdapter(arrayAdapter)

        autoCompleteSubjects.setOnTouchListener(View.OnTouchListener { v, event ->

            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= autoCompleteSubjects.right - autoCompleteSubjects.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    subjectText = autoCompleteSubjects.text.toString()
                    if (subjectText.isEmpty()) {
                        showErrorSnackBar("Enter valid Subject", clAddBranch)
                    } else {
                        for (i in 0 until subjects.size) {
                            if (subjectText == subjects[i]) {
                                isSubjectAdded = true
                                commaSeparatedSubjectIds += if (commaSeparatedSubjectIds.isNullOrEmpty())
                                    "${subjectsList[i]._id}"
                                else
                                    ",${subjectsList[i]._id}"

                                adapter.updateGenericDataList(subjectText)
                                autoCompleteSubjects.text?.clear()
                            }
                        }
                        if (!isSubjectAdded)
                            showErrorSnackBar(
                                "Select Subject from the options",
                                clAddBranch
                            )
                    }
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    override fun onItemDeleted(position: Int) {
        adapter.onRemoveItem(position)
    }

    override fun onBackPressed() {
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left)
        super.onBackPressed()
    }
}