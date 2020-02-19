package com.example.projectsetup.ui.user_details

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cell_skill.view.*

class SkillsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var onItemDeletedListener: OnItemDeletedListener

    fun setSkill(skill: String) {
        itemView.tvSkill.text = skill

        itemView.tvSkill.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (event.rawX >= itemView.tvSkill.right - itemView.tvSkill.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    if (::onItemDeletedListener.isInitialized){
                        onItemDeletedListener.onItemDeleted(adapterPosition)
                    }
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    fun setOnItemDeletedListener(onItemDeletedListener: OnItemDeletedListener){
        this.onItemDeletedListener = onItemDeletedListener
    }

    interface OnItemDeletedListener {
        fun onItemDeleted(position: Int)
    }
}