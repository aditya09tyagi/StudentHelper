package com.example.projectsetup.ui.placement.upcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.R
import com.squareup.picasso.Picasso

class UpcomingPlacementAdapter(val picasso: Picasso):RecyclerView.Adapter<UpcomingPlacementViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingPlacementViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cell_upcoming_placement,parent,false)
        return UpcomingPlacementViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holderUpcoming: UpcomingPlacementViewHolder, position: Int) {

    }
}