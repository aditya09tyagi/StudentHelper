package com.example.projectsetup.ui.company

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.data.models.Status
import com.example.projectsetup.di.components.DaggerCompanyActivityComponent
import com.example.projectsetup.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_company.*
import javax.inject.Inject

class CompanyActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var companyViewModel: CompanyViewModel
    private lateinit var companyId: String

    companion object {
        private const val EXTRA_COMPANY_ID = "EXTRA_COMPANY_ID"
        fun newIntent(context: Context, companyId: String): Intent {
            val intent = Intent(context, CompanyActivity::class.java)
            intent.putExtra(EXTRA_COMPANY_ID, companyId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArguments()
        inject()
        if (::companyId.isInitialized)
            companyViewModel.getCompanyById(companyId)
        observeData()
    }

    private fun getArguments() {
        intent?.let {
            it.getStringExtra(EXTRA_COMPANY_ID)?.let { cmpId ->
                companyId = cmpId
            }
        }
    }

    private fun inject() {
        val component = DaggerCompanyActivityComponent.builder()
            .studentHelperApplicationComponent(StudentHelper.get(this).studentHelperApplicationComponent())
            .build()

        component.injectCompanyActivityComponent(this)

        companyViewModel =
            ViewModelProvider(this, viewModelFactory).get(CompanyViewModel::class.java)
    }

    private fun observeData() {
        companyViewModel.companyLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.let { company ->
                        companyWebView.loadUrl(company.companyWebsite)
                    }
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    it.message?.let {
                        showErrorToast(it)
                    }
                }
            }
        })
    }
}