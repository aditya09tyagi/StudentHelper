package com.example.projectsetup.ui.placement.past

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.R
import com.example.projectsetup.data.models.Past
import com.squareup.picasso.Picasso

class PastPlacementAdapter(val picasso: Picasso) : RecyclerView.Adapter<PastPlacementViewHolder>() {

    private lateinit var pastList: ArrayList<Past>

    fun setList(list: ArrayList<Past>) {
        pastList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastPlacementViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cell_past_placement, parent, false)
        return PastPlacementViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (::pastList.isInitialized)
            return pastList.size
        return 0
    }

    override fun onBindViewHolder(holder: PastPlacementViewHolder, position: Int) {
        if (::pastList.isInitialized){
            holder.setPastCompany(picasso,pastList[position])
        }
    }
}