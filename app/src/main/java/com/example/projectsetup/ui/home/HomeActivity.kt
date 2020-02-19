package com.example.projectsetup.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.projectsetup.R
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.di.components.DaggerHomeActivityComponent
import com.example.projectsetup.ui.base.BaseActivity
import javax.inject.Inject

class HomeActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory:ViewModelProvider.Factory

    private lateinit var homeViewModel: HomeViewModel

    companion object{
        fun newIntent(context: Context):Intent{
            val intent = Intent(context,HomeActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        inject()
        initialiseLayout()
        setListener()
        observeData()
    }

    private fun inject(){
        val component = DaggerHomeActivityComponent.builder()
            .studentHelperApplicationComponent(StudentHelper.get(this).studentHelperApplicationComponent())
            .build()

        component.injectHomeActivity(this)

        homeViewModel = ViewModelProvider(this,viewModelFactory).get(HomeViewModel::class.java)
    }

    private fun initialiseLayout(){

    }

    private fun setListener(){

    }

    private fun observeData(){

    }
}
