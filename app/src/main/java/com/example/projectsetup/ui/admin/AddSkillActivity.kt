package com.example.projectsetup.ui.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.projectsetup.R
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.data.models.Status
import com.example.projectsetup.di.components.DaggerAddSkillActivityComponent
import com.example.projectsetup.ui.base.BaseActivity
import com.example.projectsetup.ui.loader.ProgressModal
import kotlinx.android.synthetic.main.activity_add_skill.*
import javax.inject.Inject

class AddSkillActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var progressModal: ProgressModal
    private lateinit var userId: String
    private lateinit var adminActivityViewModel: AdminActivityViewModel

    companion object {
        private const val EXTRA_USER_ID = "EXTRA_USER_ID"
        fun newIntent(context: Context, userId: String): Intent {
            val intent = Intent(context, AddSkillActivity::class.java)
            intent.putExtra(EXTRA_USER_ID, userId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_skill)
        setWindowInsets()
        inject()
        getArguments()
        setListener()
        observeData()
    }

    private fun setWindowInsets() {
        clHome.setOnApplyWindowInsetsListener { view, insets ->
            view.setPadding(0, insets.systemWindowInsetTop, 0, insets.systemWindowInsetBottom)
            insets
        }
    }

    private fun inject() {
        val component = DaggerAddSkillActivityComponent.builder()
            .studentHelperApplicationComponent(StudentHelper.get(this).studentHelperApplicationComponent())
            .build()
        component.injectAddSkillActivity(this)
        adminActivityViewModel =
            ViewModelProvider(this, viewModelFactory).get(AdminActivityViewModel::class.java)
        progressModal = ProgressModal(this)
    }

    private fun getArguments() {
        intent?.let {
            it.getStringExtra(EXTRA_USER_ID)?.let { uid ->
                userId = uid
            }
        }
    }

    private fun setListener() {
        btnAddSkill.setOnClickListener {
            val skillName = etSkillName.text.toString()
            if (::userId.isInitialized){
                adminActivityViewModel.addSkill(skillName, userId)
            }
        }
    }

    private fun observeData() {
        adminActivityViewModel.addSkillLiveData.observe(this, Observer {
            when(it.status){
                Status.LOADING->{
                    progressModal.show()
                }
                Status.SUCCESS->{
                    progressModal.dismiss()
                    it.data?.let {skill ->
                        showSuccessToast("${skill.name} skill added successfully")
                    }
                }
                Status.ERROR->{
                    progressModal.dismiss()
                    it.message?.let {msg->
                        showErrorToast(msg)
                    }
                }
            }
        })
    }
}