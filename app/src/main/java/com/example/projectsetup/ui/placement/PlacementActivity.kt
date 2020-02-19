package com.example.projectsetup.ui.placement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectsetup.R
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.di.components.DaggerPlacementActivityComponent
import com.example.projectsetup.ui.base.BaseActivity
import com.example.projectsetup.ui.placement.past.PastPlacementAdapter
import com.example.projectsetup.ui.placement.upcoming.UpcomingPlacementAdapter
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

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, PlacementActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placement)
        inject()
        initialiseLayout()
        setListener()
        observeData()
    }

    private fun inject() {
        val component = DaggerPlacementActivityComponent.builder()
            .studentHelperApplicationComponent(StudentHelper.get(this).studentHelperApplicationComponent())
            .build()

        component.injectPlacementActivityComponent(this)

        placementViewModel =
            ViewModelProvider(this, viewModelFactory).get(PlacementViewModel::class.java)

    }

    private fun initialiseLayout() {
        rvUpcoming.layoutManager = LinearLayoutManager(this)
        rvUpcoming.adapter = upcomingPlacementAdapter

        rvPast.layoutManager = LinearLayoutManager(this)
        rvPast.adapter = pastPlacementAdapter
    }

    private fun setListener() {

    }

    private fun observeData() {

    }
}