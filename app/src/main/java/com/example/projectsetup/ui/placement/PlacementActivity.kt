package com.example.projectsetup.ui.placement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectsetup.R
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.data.models.Status
import com.example.projectsetup.di.components.DaggerPlacementActivityComponent
import com.example.projectsetup.ui.base.BaseActivity
import com.example.projectsetup.ui.company.CompanyActivity
import com.example.projectsetup.ui.loader.ProgressModal
import com.example.projectsetup.ui.placement.live.LiveCompaniesAdapter
import com.example.projectsetup.ui.placement.past.PastPlacementAdapter
import com.example.projectsetup.ui.placement.upcoming.UpcomingPlacementAdapter
import com.example.projectsetup.util.Constants
import kotlinx.android.synthetic.main.activity_placement.*
import javax.inject.Inject

class PlacementActivity : BaseActivity(), UpcomingPlacementAdapter.OnUpcomingItemClickListener,
    PastPlacementAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var upcomingPlacementAdapter: UpcomingPlacementAdapter

    @Inject
    lateinit var liveCompaniesAdapter: LiveCompaniesAdapter

    @Inject
    lateinit var pastPlacementAdapter: PastPlacementAdapter

    private lateinit var placementViewModel: PlacementViewModel
    private lateinit var progressModal: ProgressModal
    private lateinit var userId: String

    companion object {
        private const val EXTRA_USER_ID = "EXTRA_USER_ID"
        fun newIntent(context: Context, userId: String): Intent {
            val intent = Intent(context, PlacementActivity::class.java)
            intent.putExtra(EXTRA_USER_ID, userId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placement)
        getArguments()
        inject()
        initialiseLayout()
        getLiveCompanies()
        getUpcomingCompanies()
        getPastCompanies()
        setListener()
        observeData()
    }

    private fun getArguments() {
        intent?.let {
            it.getStringExtra(EXTRA_USER_ID)?.let { uid ->
                userId = uid
            }
        }
    }

    private fun inject() {
        val component = DaggerPlacementActivityComponent.builder()
            .studentHelperApplicationComponent(StudentHelper.get(this).studentHelperApplicationComponent())
            .build()

        component.injectPlacementActivityComponent(this)

        placementViewModel =
            ViewModelProvider(this, viewModelFactory).get(PlacementViewModel::class.java)

        progressModal = ProgressModal(this)

    }

    private fun initialiseLayout() {
        rvUpcoming.layoutManager = LinearLayoutManager(this)
        rvUpcoming.adapter = upcomingPlacementAdapter
        upcomingPlacementAdapter.setOnUpcomingItemClickListener(this)

        rvPast.layoutManager = LinearLayoutManager(this)
        rvPast.adapter = pastPlacementAdapter
        pastPlacementAdapter.setOnItemClickListener(this)

        rvLive.layoutManager = LinearLayoutManager(this)
        rvLive.adapter = liveCompaniesAdapter

    }

    private fun getUpcomingCompanies() {
        if (::userId.isInitialized)
            placementViewModel.upcomingCompanies(userId)
    }

    private fun getPastCompanies() {
        if (::userId.isInitialized)
            placementViewModel.pastCompanies(userId)
    }

    private fun getLiveCompanies() {
        if (::userId.isInitialized)
            placementViewModel.liveCompanies(userId)
    }

    private fun setListener() {

    }

    private fun observeData() {
        placementViewModel.pastLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressModal.show()
                }
                Status.SUCCESS -> {
                    progressModal.dismiss()
                    it.data?.let { list ->
                        if (list.isNotEmpty()) {
                            tvPast.visibility = View.VISIBLE
                            pastPlacementAdapter.setList(list)
                        } else {
                            tvPast.visibility = View.GONE
                        }
                    }
                }
                Status.ERROR -> {
                    progressModal.dismiss()
                }
            }
        })
        placementViewModel.upcomingLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressModal.show()
                }
                Status.SUCCESS -> {
                    progressModal.dismiss()
                    it.data?.let { list ->
                        if (list.isNotEmpty()) {
                            tvUpcoming.visibility = View.VISIBLE
                            upcomingPlacementAdapter.setList(list)
                        } else {
                            tvUpcoming.visibility = View.GONE
                        }
                    }
                }
                Status.ERROR -> {
                    progressModal.dismiss()
                }
            }
        })
        placementViewModel.liveCompaniesLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressModal.show()
                }
                Status.SUCCESS -> {
                    progressModal.dismiss()
                    it.data?.let { list ->
                        if (list.isNotEmpty()) {
                            tvLive.visibility = View.VISIBLE
                            liveCompaniesAdapter.setList(list)
                        } else {
                            tvLive.visibility = View.GONE
                        }
                    }
                }
                Status.ERROR -> {
                    progressModal.dismiss()
                }
            }
        })
        placementViewModel.jobApplyLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressModal.show()
                }
                Status.SUCCESS -> {
                    progressModal.dismiss()
                    it.data?.let { job ->
                        showSuccessToast("Registered")
                    }
                }
                Status.ERROR -> {
                    progressModal.dismiss()
                }
            }
        })
    }

    override fun onRegisterNowClickedListener(position: Int, jobId: String) {
        if (::userId.isInitialized)
            placementViewModel.applyForJob(userId, jobId)
    }

    override fun onItemClickListener(position: Int, companyId: String) {
        startCompanyActivity(companyId)
    }

    override fun onItemClick(position: Int, companyId: String) {
        startCompanyActivity(companyId)
    }

    private fun startCompanyActivity(companyId: String){
        startActivity(CompanyActivity.newIntent(this, companyId))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}