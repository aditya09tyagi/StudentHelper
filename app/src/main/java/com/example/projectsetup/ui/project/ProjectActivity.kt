package com.example.projectsetup.ui.project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.projectsetup.R
import com.example.projectsetup.ui.base.BaseActivity
import kotlinx.android.synthetic.main.acitivity_project.*

class ProjectActivity : BaseActivity() {

    companion object{
        fun newIntent(context: Context): Intent {
            val intent =  Intent(context,ProjectActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_project)
        inject()
        initialiseLayout()
        setListener()
        observeData()
    }

    private fun inject(){

    }

    private fun initialiseLayout(){

    }

    private fun setListener(){
        fabChat.setOnClickListener {

        }
    }

    private fun observeData(){

    }
}