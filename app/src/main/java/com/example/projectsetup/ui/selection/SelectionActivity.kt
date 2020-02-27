package com.example.projectsetup.ui.selection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.projectsetup.R
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.di.components.DaggerSelectionActivityComponent
import com.example.projectsetup.ui.base.BaseActivity
import com.example.projectsetup.ui.placement.PlacementActivity
import com.example.projectsetup.ui.project.ProjectActivity
import kotlinx.android.synthetic.main.activity_selection.*

class SelectionActivity : BaseActivity() {

    private lateinit var userId: String

    companion object {
        private const val EXTRA_USER_ID = "EXTRA_USER_ID"
        fun newIntent(context: Context, userId: String): Intent {
            val intent = Intent(context, SelectionActivity::class.java)
            intent.putExtra(EXTRA_USER_ID, userId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
        getArguments()
        inject()
        initialiseLayout()
        setListener()
    }

    private fun getArguments() {
        intent?.let {
            it.getStringExtra(EXTRA_USER_ID)?.let { uid ->
                userId = uid
            }
        }
    }

    private fun inject() {
        val component = DaggerSelectionActivityComponent.builder()
            .studentHelperApplicationComponent(StudentHelper.get(this).studentHelperApplicationComponent())
            .build()

        component.injectSelectionActivityComponent(this)
    }

    private fun initialiseLayout() {}

    private fun setListener() {
        ivPlacement.setOnClickListener {
            if (::userId.isInitialized){
                startActivity(PlacementActivity.newIntent(this,userId))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }

        ivProject.setOnClickListener {
            if (::userId.isInitialized){
                startActivity(ProjectActivity.newIntent(this,userId))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

}