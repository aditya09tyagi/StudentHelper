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

    companion object{
        fun newIntent(context: Context): Intent {
            val intent =  Intent(context,SelectionActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
        inject()
        initialiseLayout()
        setListener()
    }

    private fun inject(){
        val component = DaggerSelectionActivityComponent.builder()
            .studentHelperApplicationComponent(StudentHelper.get(this).studentHelperApplicationComponent())
            .build()

        component.injectSelectionActivityComponent(this)
    }

    private fun initialiseLayout(){}

    private fun setListener(){
        ivPlacement.setOnClickListener {
            startActivity(PlacementActivity.newIntent(this))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        ivProject.setOnClickListener {
            startActivity(ProjectActivity.newIntent(this))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

}