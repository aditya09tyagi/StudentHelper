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
import com.example.projectsetup.data.models.Skill
import com.example.projectsetup.data.models.Status
import com.example.projectsetup.di.components.DaggerAddJobActivityComponent
import com.example.projectsetup.ui.base.BaseActivity
import com.example.projectsetup.ui.dialog.JobDateTimeDialog
import com.example.projectsetup.ui.generic_rv.GenericDataAdapter
import com.example.projectsetup.ui.loader.ProgressModal
import com.example.projectsetup.util.CustomValidationListener
import com.example.projectsetup.util.DateTimeUtils
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.activity_add_job.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

class AddJobActivity : BaseActivity(), GenericDataAdapter.OnItemDeletedListener,
    JobDateTimeDialog.OnSubmitClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var adapter: GenericDataAdapter

    @NotEmpty
    private lateinit var etCompanyName: MaterialEditText
    @NotEmpty
    private lateinit var etJobTitle: MaterialEditText
    @NotEmpty
    private lateinit var etJobDescription: MaterialEditText
    @NotEmpty
    private lateinit var etJobPlace: MaterialEditText

    private lateinit var autoCompleteSkills: AutoCompleteTextView
    private var skillList: ArrayList<Skill> = ArrayList()
    private lateinit var skillText: String
    private lateinit var userId: String
    private var isSkillAdded: Boolean = false
    private lateinit var validator: Validator
    private lateinit var progressModal: ProgressModal
    private var commaSeparatedSkillIds: String = ""
    private var skills: ArrayList<String> = ArrayList()
    private lateinit var dateTimeDialog: JobDateTimeDialog
    private lateinit var zonedDateTime: ZonedDateTime
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    private var hours: Int = 0
    private var minutes: Int = 0

    private lateinit var adminActivityViewModel: AdminActivityViewModel

    companion object {
        private const val EXTRA_USER_ID = "EXTRA_USER_ID"
        fun newIntent(context: Context, userId: String): Intent {
            val intent = Intent(context, AddJobActivity::class.java)
            intent.putExtra(EXTRA_USER_ID, userId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_job)
        setWindowInsets()
        getArguments()
        inject()
        initialiseLayout()
        setListener()
        observeData()
    }

    private fun setWindowInsets() {
        clJobContainer.setOnApplyWindowInsetsListener { view, insets ->
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
        val component = DaggerAddJobActivityComponent.builder()
            .studentHelperApplicationComponent(StudentHelper.get(this).studentHelperApplicationComponent())
            .build()
        component.injectAddJobActivity(this)
        validator = Validator(this)
        adminActivityViewModel =
            ViewModelProvider(this, viewModelFactory).get(AdminActivityViewModel::class.java)
    }

    private fun initialiseLayout() {
        etCompanyName = findViewById(R.id.etCompanyName)
        etJobTitle = findViewById(R.id.etTitle)
        etJobDescription = findViewById(R.id.etDescription)
        etJobPlace = findViewById(R.id.etPlace)
        autoCompleteSkills = findViewById(R.id.autoCompleteSkills)

        rvSkills.layoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        rvSkills.adapter = adapter
        initDialog()
        adminActivityViewModel.searchSkills("")
    }

    private fun initDialog() {
        zonedDateTime = DateTimeUtils.getLocalDate()
        supportFragmentManager.let { fm ->
            dateTimeDialog =
                JobDateTimeDialog(
                    this,
                    zonedDateTime = zonedDateTime,
                    fragmentManager = fm
                )
        }

        dateTimeDialog.setOnSubmitClickListener(this)
    }

    private fun setListener() {
        btnAddJob.setOnClickListener {
            validator.validate()
        }

        tvAddDateTime.setOnClickListener {
            if (::dateTimeDialog.isInitialized) {
                dateTimeDialog.show()
            }
        }

        validator.setValidationListener(object :
            CustomValidationListener(context = this@AddJobActivity) {
            override fun onValidationSucceeded() {
                if (isSkillAdded) {
                    if (::userId.isInitialized) {
                        val companyName = etCompanyName.text.toString()
                        val jobTitle = etJobTitle.text.toString()
                        val jobDescription = etJobDescription.text.toString()
                        val jobPlace = etJobPlace.text.toString()
                        adminActivityViewModel.addJob(
                            commaSeparatedSkillIds = commaSeparatedSkillIds,
                            companyName = companyName,
                            companyDescription = jobDescription,
                            companyTitle = jobTitle,
                            driveLocation = jobPlace,
                            driveDay = day,
                            driveMonth = month,
                            driveYear = year,
                            driveHour = hours,
                            driveMinute = minutes,
                            userId = userId
                        )
                    }
                } else {
                    autoCompleteSkills.error = "Enter atleast one skill"
                }
            }
        })

        adapter.setOnItemDeletedListener(this)
    }

    private fun observeData() {
        adminActivityViewModel.getSkillsLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    it.data?.let { skills ->
                        this.skillList = skills
                        initAutoComplete()
                    }

                }
                Status.ERROR -> {

                }
            }
        })
        adminActivityViewModel.addJobLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressModal.show()
                }
                Status.SUCCESS -> {
                    progressModal.dismiss()
                    it.data?.let { job ->
                        showSuccessToast("${job.jobTitle} job is added successfully")
                    }
                }
                Status.ERROR -> {
                    progressModal.dismiss()
                    it.message?.let { msg ->
                        showErrorToast(msg)
                    }
                }
            }
        })
    }

    private fun initAutoComplete() {

        if (skillList.isNotEmpty()) {
            skillList.forEachIndexed { index, skill ->
                skills.add(skill.name)
            }
        }

        val arrayAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, skills)

        autoCompleteSkills.setAdapter(arrayAdapter)

        autoCompleteSkills.setOnTouchListener(View.OnTouchListener { v, event ->

            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= autoCompleteSkills.right - autoCompleteSkills.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    skillText = autoCompleteSkills.text.toString()
                    if (skillText.isEmpty()) {
                        showErrorSnackBar("Enter valid skill", clJobContainer)
                    } else {
                        for (i in 0 until skills.size) {
                            if (skillText == skills[i]) {
                                isSkillAdded = true
                                commaSeparatedSkillIds += if (commaSeparatedSkillIds.isNullOrEmpty())
                                    "${skillList[i].id}"
                                else
                                    ",${skillList[i].id}"

                                adapter.updateGenericDataList(skillText)
                                autoCompleteSkills.text?.clear()
                            }
                        }
                        if (!isSkillAdded)
                            showErrorSnackBar(
                                "Select skill from the options",
                                clJobContainer
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

    override fun onSubmitClick(year: Int, month: Int, day: Int, hours: Int, minutes: Int) {
        this.year = year
        this.month = month
        this.day = day
        this.hours = hours
        this.minutes = minutes
        dateTimeDialog.dismiss()
    }
}