package com.example.projectsetup.ui.project.rv_project

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.data.models.Project
import com.example.projectsetup.util.DateTimeUtils
import kotlinx.android.synthetic.main.cell_project.view.*
import org.threeten.bp.format.DateTimeFormatter

class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var onItemClickListener: OnItemClickListener

    fun setProject(project: Project) {
        itemView.tvRvProjectName.text = project.title
        var members = ""
        project.userMembers.forEachIndexed { index, user ->
            if (index == project.userMembers.size - 1) {
                members += user.name
            } else {
                members = members + user.name + "\n"
            }
        }
        itemView.tvRvMembers.text = members
        itemView.tvRvCoordinatorName.text = project.userFaculty.name
        itemView.tvRvDeadline.text =
            DateTimeUtils.getLocalDateFromString(project.deadline)
                .format(
                    DateTimeFormatter.BASIC_ISO_DATE
                )
        itemView.projectRvProgress.progress = project.progress
        itemView.tvRvProgressValue.text = project.progress.toString()

        itemView.btnUpdateProgress.setOnClickListener {
            if (::onItemClickListener.isInitialized)
                onItemClickListener.onUpdateClickListener(projectId = project.id,position = adapterPosition)
        }
        itemView.ivChat.setOnClickListener {
            if (::onItemClickListener.isInitialized)
                onItemClickListener.onChatClickListener(projectId = project.id,position = adapterPosition)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onUpdateClickListener(projectId: String, position: Int)
        fun onChatClickListener(projectId: String, position: Int)
    }
}