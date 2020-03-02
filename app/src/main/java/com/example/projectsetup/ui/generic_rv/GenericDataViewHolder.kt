package com.example.projectsetup.ui.generic_rv

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cell_generic.view.*

class GenericDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var onItemDeletedListener: OnItemDeletedListener

    fun setGenericData(genericData: String) {
        itemView.tvGenericName.text = genericData

        itemView.tvGenericName.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (event.rawX >= itemView.tvGenericName.right - itemView.tvGenericName.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
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