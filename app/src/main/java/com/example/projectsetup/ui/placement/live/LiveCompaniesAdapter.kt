package com.example.projectsetup.ui.placement.live

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.R
import com.example.projectsetup.data.models.Live
import com.squareup.picasso.Picasso

class LiveCompaniesAdapter(private val picasso: Picasso) :RecyclerView.Adapter<LiveCompanyViewHolder>(){


    private lateinit var liveCompaniesList: ArrayList<Live>

    fun setList(list: ArrayList<Live>){
        liveCompaniesList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveCompanyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cell_live_companies,parent,false)
        return LiveCompanyViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (::liveCompaniesList.isInitialized)
            return liveCompaniesList.size
        return 0
    }

    override fun onBindViewHolder(holder: LiveCompanyViewHolder, position: Int) {
        if (::liveCompaniesList.isInitialized){
            holder.setLiveCompany(picasso,liveCompaniesList[position])
        }
    }

}