package com.example.projectsetup.ui.user_details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.R
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.di.components.DaggerUserDetailsActivityComponent
import com.example.projectsetup.ui.base.BaseActivity
import com.example.projectsetup.ui.selection.SelectionActivity
import com.example.projectsetup.util.CustomValidationListener
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.activity_user_details.*
import javax.inject.Inject


class UserDetailsActivity : BaseActivity(), SkillsAdapter.OnItemDeletedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var adapter: SkillsAdapter

    private lateinit var skillText: String

    @NotEmpty
    private lateinit var etAge: MaterialEditText

    private lateinit var etSkill: MaterialEditText
    private lateinit var name: String
    private var isSkillAdded: Boolean = false
    private lateinit var validator: Validator

    companion object {
        const val EXTRA_NAME = "EXTRA_NAME"
        fun newIntent(context: Context, name: String): Intent {
            val intent = Intent(context, UserDetailsActivity::class.java)
            intent.putExtra(EXTRA_NAME, name)
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
            it.getStringExtra(EXTRA_NAME)?.let { name ->
                this.name = name
            }
        }
    }

    private fun inject() {
        val component = DaggerUserDetailsActivityComponent.builder()
            .studentHelperApplicationComponent(StudentHelper.get(this).studentHelperApplicationComponent())
            .build()

        component.injectUserDetailsActivityComponent(this)

    }

    private fun initialiseLayout() {
        etAge = findViewById(R.id.etAge)
        etSkill = findViewById(R.id.etSkills)

        if (::name.isInitialized)
            tvHiUser.text = "Hi $name,please fill the following details."

        validator = Validator(this)

        ArrayAdapter.createFromResource(
            this,
            R.array.branch_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerBranch.adapter = adapter
        }
        ArrayAdapter.createFromResource(
            this,
            R.array.section_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerSection.adapter = adapter
        }
        ArrayAdapter.createFromResource(
            this,
            R.array.semester_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerSemester.adapter = adapter
        }

        rvSkills.layoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        rvSkills.adapter = adapter

    }

    private fun setListener() {

        btnSubmitDetails.setOnClickListener {
            validator.validate()
        }

        validator.setValidationListener(object :
            CustomValidationListener(context = this@UserDetailsActivity) {
            override fun onValidationSucceeded() {
                if (isSkillAdded) {
                    startSelectionActivity()
                } else {
                    etSkill.error = "Enter atleast one skill"
                }
            }
        })

        adapter.setOnItemDeletedListener(this)

        etSkills.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etSkills.right - etSkills.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    skillText = etSkills.text.toString()
                    if (skillText.isEmpty()) {
                        showErrorSnackBar("Enter valid skill", clUserDetails)
                    } else {
                        isSkillAdded = true
                        adapter.updateSkillsList(skillText)
                        etSkills.text?.clear()
                    }
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private fun observeData() {

    }

    private fun startSelectionActivity() {
        startActivity(SelectionActivity.newIntent(this))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onItemDeleted(position: Int) {
        adapter.onRemoveItem(position)
    }

    override fun onBackPressed() {
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left)
        super.onBackPressed()
    }
}