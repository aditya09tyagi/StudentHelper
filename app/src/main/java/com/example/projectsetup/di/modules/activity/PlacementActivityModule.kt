package com.example.projectsetup.di.modules.activity

import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.placement.live.LiveCompaniesAdapter
import com.example.projectsetup.ui.placement.past.PastPlacementAdapter
import com.example.projectsetup.ui.placement.upcoming.UpcomingPlacementAdapter
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides

@Module
class PlacementActivityModule {

    @Provides
    @PerFragmentScope
    fun setUpcomingAdapter(picasso: Picasso): UpcomingPlacementAdapter {
        return UpcomingPlacementAdapter(picasso)
    }

    @Provides
    @PerFragmentScope
    fun setPastAdapter(picasso: Picasso):PastPlacementAdapter{
        return PastPlacementAdapter(picasso)
    }

    @Provides
    @PerFragmentScope
    fun setLiveCompaniesAdapter(picasso: Picasso):LiveCompaniesAdapter{
        return LiveCompaniesAdapter(picasso)
    }
}