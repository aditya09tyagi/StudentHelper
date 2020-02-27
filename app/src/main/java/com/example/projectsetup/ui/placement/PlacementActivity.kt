package com.example.projectsetup.ui.placement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectsetup.R
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.data.models.Status
import com.example.projectsetup.di.components.DaggerPlacementActivityComponent
import com.example.projectsetup.ui.base.BaseActivity
import com.example.projectsetup.ui.loader.ProgressModal
import com.example.projectsetup.ui.placement.past.PastPlacementAdapter
import com.example.projectsetup.ui.placement.upcoming.UpcomingPlacementAdapter
import com.example.projectsetup.util.Constants
import kotlinx.android.synthetic.main.activity_placement.*
import javax.inject.Inject

class PlacementActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var upcomingPlacementAdapter: UpcomingPlacementAdapter

    @Inject
    lateinit var pastPlacementAdapter: PastPlacementAdapter

    private lateinit var placementViewModel: PlacementViewModel
    private lateinit var progressModal: ProgressModal
    private lateinit var userId: String

    companion object {
        private const val EXTRA_USER_ID = "EXTRA_USER_ID"
        fun newIntent(context: Context, userId: String): Intent {
            val intent = Intent(context, PlacementActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placement)
        getArguments()
        inject()
        initialiseLayout()
        setListener()
        observeData()
    }

    private fun getArguments(){
        intent?.let {
            it.getStringExtra(EXTRA_USER_ID)?.let {uid->
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

        rvPast.layoutManager = LinearLayoutManager(this)
        rvPast.adapter = pastPlacementAdapter

        placementViewModel.upcomingCompanies(sharedPreferenceUtil.getString(Constants.EXTRA_USER_ID))
    }

    private fun setListener() {

    }

    private fun observeData() {
        placementViewModel.upcomingLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressModal.show()
                }
                Status.SUCCESS -> {
                    progressModal.dismiss()
                    it.data?.let { list ->
                        upcomingPlacementAdapter.setList(list)
                    }
                }
                Status.ERROR -> {
                    progressModal.dismiss()
                }
            }
        })
    }
}