package com.example.projectsetup.ui.user_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.R

class SkillsAdapter:RecyclerView.Adapter<SkillsViewHolder>(),
    SkillsViewHolder.OnItemDeletedListener {

    private lateinit var onItemDeletedListener: OnItemDeletedListener
    private var skillsList:ArrayList<String> = ArrayList()

    fun updateSkillsList(skill:String){
        skillsList.add(skill)
        notifyDataSetChanged()
    }

    fun onRemoveItem(position: Int){
        skillsList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cell_skill,parent,false)
        return SkillsViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (skillsList.isNotEmpty())
            return skillsList.size
        return 0
    }

    override fun onBindViewHolder(holder: SkillsViewHolder, position: Int) {
        if (skillsList.isNotEmpty()){
            holder.setSkill(skillsList[position])
            holder.setOnItemDeletedListener(this)
        }
    }

    fun setOnItemDeletedListener(onItemDeletedListener: OnItemDeletedListener){
        this.onItemDeletedListener = onItemDeletedListener
    }

    interface OnItemDeletedListener {
        fun onItemDeleted(position: Int)
    }

    override fun onItemDeleted(position: Int) {
        if(::onItemDeletedListener.isInitialized){
            onItemDeletedListener.onItemDeleted(position)
        }
    }
}