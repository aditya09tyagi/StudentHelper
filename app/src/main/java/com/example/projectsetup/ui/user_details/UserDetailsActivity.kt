package com.example.projectsetup.ui.user_details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.R
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.data.models.Branch
import com.example.projectsetup.data.models.Skill
import com.example.projectsetup.data.models.Status
import com.example.projectsetup.di.components.DaggerUserDetailsActivityComponent
import com.example.projectsetup.ui.base.BaseActivity
import com.example.projectsetup.ui.generic_rv.GenericDataAdapter
import com.example.projectsetup.ui.loader.ProgressModal
import com.example.projectsetup.ui.selection.SelectionActivity
import com.example.projectsetup.util.Constants
import com.example.projectsetup.util.CustomValidationListener
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.activity_user_details.*
import javax.inject.Inject


class UserDetailsActivity : BaseActivity(), GenericDataAdapter.OnItemDeletedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var adapter: GenericDataAdapter

    @NotEmpty
    private lateinit var etAge: MaterialEditText

    private lateinit var skillText: String

    private lateinit var userDetailsViewModel: UserDetailsViewModel
    private lateinit var autoCompleteSkills: AutoCompleteTextView
    private var skillList: ArrayList<Skill> = ArrayList()
    private lateinit var name: String
    private lateinit var userId: String
    private var isSkillAdded: Boolean = false
    private lateinit var validator: Validator
    private lateinit var progressModal: ProgressModal
    private var commaSeparatedSkillIds: String = ""
    private var skills: ArrayList<String> = ArrayList()

    private lateinit var branchList: List<Branch>

    private var userType = Constants.USER_TYPE_STUDENT

    companion object {
        private const val EXTRA_USER_TYPE = "EXTRA_USER_TYPE"
        private const val EXTRA_USER_NAME = "EXTRA_USER_NAME"
        private const val EXTRA_USER_ID = "EXTRA_USER_ID"
        fun newIntent(context: Context, userId: String, name: String, userType: Int): Intent {
            val intent = Intent(context, UserDetailsActivity::class.java)
            intent.putExtra(EXTRA_USER_ID, userId)
            intent.putExtra(EXTRA_USER_NAME, name)
            intent.putExtra(EXTRA_USER_TYPE, userType)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
        getArguments()
        inject()
        initialiseLayout()
        setListener()
        observeData()
    }

    private fun getArguments() {
        intent?.let {
            it.getStringExtra(EXTRA_USER_NAME)?.let { name ->
                this.name = name
            }
            it.getStringExtra(EXTRA_USER_ID)?.let { id ->
                this.userId = id
            }
            it.getIntExtra(EXTRA_USER_TYPE, Constants.USER_TYPE_STUDENT).let { id ->
                this.userType = id
            }
        }
    }

    private fun inject() {
        val component = DaggerUserDetailsActivityComponent.builder()
            .studentHelperApplicationComponent(StudentHelper.get(this).studentHelperApplicationComponent())
            .build()

        component.injectUserDetailsActivityComponent(this)

        userDetailsViewModel =
            ViewModelProvider(this, viewModelFactory).get(UserDetailsViewModel::class.java)

        progressModal = ProgressModal(this)
    }

    private fun initialiseLayout() {
        etAge = findViewById(R.id.etAge)
        autoCompleteSkills = findViewById(R.id.autoCompleteSkills)

        userDetailsViewModel.getBranches()

        if (::name.isInitialized)
            tvHiUser.text = "Hi $name, please fill the following details."

        validator = Validator(this)

        ArrayAdapter.createFromResource(
            this,
            R.array.section_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerSection.adapter = adapter
        }
        ArrayAdapter.createFromResource(
            this,
            R.array.semester_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerSemester.adapter = adapter
        }

        rvSkills.layoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        rvSkills.adapter = adapter

    }

    private fun setListener() {

        userDetailsViewModel.searchSkills("")

        btnSubmitDetails.setOnClickListener {
            validator.validate()
        }

        validator.setValidationListener(object :
            CustomValidationListener(context = this@UserDetailsActivity) {
            override fun onValidationSucceeded() {
                if (isSkillAdded) {
                    if (::userId.isInitialized) {
                        userDetailsViewModel.updateUser(
                            userId = userId,
                            age = etAge.text.toString().toInt(),
                            branch = branchList[spinnerBranch.selectedItemPosition].id,
                            section = spinnerSection.selectedItem.toString(),
                            semester = spinnerSemester.selectedItem.toString().toInt(),
                            commaSeparatedSkillIds = commaSeparatedSkillIds,
                            userType = userType
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
        userDetailsViewModel.skillsLiveData.observe(this, Observer {
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

        userDetailsViewModel.updateUserLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressModal.show()
                }
                Status.SUCCESS -> {
                    progressModal.dismiss()
                    it.data?.let {
                        startSelectionActivity()
                    }
                }
                Status.ERROR -> {
                    progressModal.hide()
                }
            }
        })

        userDetailsViewModel.branchLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    it.data?.let {
                        branchList = it
                        val newList = ArrayList<String>()
                        branchList.forEach {
                            newList.add(it.branchName)
                        }
                        val adapter = ArrayAdapter<String>(
                            this,
                            android.R.layout.simple_spinner_item,
                            newList
                        )
                        spinnerBranch.adapter = adapter
                    }

                }
                Status.ERROR -> {
                    showErrorToast("${it.message}")
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

        autoCompleteSkills.setOnTouchListener(OnTouchListener { v, event ->

            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= autoCompleteSkills.right - autoCompleteSkills.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    skillText = autoCompleteSkills.text.toString()
                    if (skillText.isEmpty()) {
                        showErrorSnackBar("Enter valid skill", clUserDetails)
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
                                clUserDetails
                            )
                    }
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private fun startSelectionActivity() {
        startActivity(SelectionActivity.newIntent(this, userId))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finishAffinity()
    }

    override fun onItemDeleted(position: Int) {
        adapter.onRemoveItem(position)
    }

    override fun onBackPressed() {
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left)
        super.onBackPressed()
    }
}