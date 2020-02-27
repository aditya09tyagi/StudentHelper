package com.example.projectsetup.ui.chat

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.R
import com.example.projectsetup.data.models.ChatMessage
import com.example.projectsetup.util.DateTimeUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cell_self_chat.view.*

class SelfChatViewHolder(itemView:View, private val picasso: Picasso):RecyclerView.ViewHolder(itemView) {
    fun setChatMessage(chatMessage: ChatMessage) {
        itemView.messageTv.text = chatMessage.message
        itemView.userNameTv.text = chatMessage.userName
        chatMessage.dataTime?.let {
            try {
                itemView.timeTv.text = DateTimeUtils.getDateFromMillis(it as Long)
            } catch (e: Exception) {
            }
        }
        chatMessage.userAvatar?.let {
            if (it.isNotBlank() && it.isNotEmpty()) {
                picasso.load(it).placeholder(R.drawable.ic_avatar).error(R.drawable.ic_avatar).into(itemView.civSelf)
            } else {
                itemView.civSelf.setImageResource(R.drawable.ic_avatar)
            }
        } ?: run {
            itemView.civSelf.setImageResource(R.drawable.ic_avatar)
        }
    }
}