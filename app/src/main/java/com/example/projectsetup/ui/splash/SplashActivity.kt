package com.example.projectsetup.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.projectsetup.R
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.di.components.DaggerSplashActivityComponent
import com.example.projectsetup.ui.admin.AdminHomeActivity
import com.example.projectsetup.ui.base.BaseActivity
import com.example.projectsetup.ui.home.HomeActivity
import com.example.projectsetup.ui.login.LoginActivity
import com.example.projectsetup.ui.selection.SelectionActivity
import com.example.projectsetup.util.Constants
import com.example.projectsetup.util.SharedPreferencesLiveData
import com.example.projectsetup.util.SharedPreferencesUserLiveData
import com.jaeger.library.StatusBarUtil
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var sharedPreferencesLiveData: SharedPreferencesUserLiveData

    private lateinit var splashViewModel: SplashViewModel
    private lateinit var userId: String

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, SplashActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setColorNoTranslucent(
            this,
            ContextCompat.getColor(this, R.color.white)
        )

        inject()
        splashViewModel.isUserLoggedIn()
        observeData()
    }

    private fun inject() {
        val component = DaggerSplashActivityComponent.builder()
            .studentHelperApplicationComponent(StudentHelper.get(this).studentHelperApplicationComponent())
            .build()
        component.injectSplashActivityComponent(this)

        splashViewModel = ViewModelProvider(this, viewModelFactory).get(SplashViewModel::class.java)
    }

    private fun observeData() {
        sharedPreferencesLiveData.observe(this, Observer {
            it?.let {
                when (it.userType) {
                    Constants.USER_TYPE_STUDENT -> {
                        userId = sharedPreferenceUtil.getString(Constants.EXTRA_USER_ID)
                        goToSelectionActivity()
                    }
                    Constants.USER_TYPE_FACULTY -> {

                    }
                    Constants.USER_TYPE_ADMIN -> {
                        startActivity(AdminHomeActivity.newIntent(this))
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finishAffinity()
                    }
                }
            } ?: run {
                goToLoginActivity()
            }
        })
    }

    private fun goToSelectionActivity() {
        if (::userId.isInitialized) {
            startActivity(SelectionActivity.newIntent(this, userId))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finishAffinity()
        }
    }

    private fun goToLoginActivity() {
        startActivity(LoginActivity.newIntent(this))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finishAffinity()
    }
}