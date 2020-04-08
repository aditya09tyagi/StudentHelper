package com.example.projectsetup.ui.project.rv_project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.R
import com.example.projectsetup.data.models.Project

class ProjectAdapter : RecyclerView.Adapter<ProjectViewHolder>(),
    ProjectViewHolder.OnItemClickListener {

    private var userType: Int = 0
    private var projectList: ArrayList<Project> = ArrayList()
    private lateinit var onItemClickListener: OnItemClickListener

    fun setProjectList(list: ArrayList<Project>, userType: Int) {
        projectList = list
        this.userType = userType
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cell_project, parent, false)
        return ProjectViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (projectList.isNotEmpty())
            return projectList.size
        return 0
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        if (projectList.isNotEmpty()) {
            holder.setProject(projectList[position],userType)
            holder.setOnItemClickListener(this)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onUpdateClickListener(projectId: String, position: Int)
        fun onChatClickListener(projectId: String, position: Int)
    }

    override fun onUpdateClickListener(projectId: String, position: Int) {
        if (::onItemClickListener.isInitialized)
            onItemClickListener.onUpdateClickListener(projectId, position)
    }

    override fun onChatClickListener(projectId: String, position: Int) {
        if (::onItemClickListener.isInitialized)
            onItemClickListener.onChatClickListener(projectId, position)
    }
}