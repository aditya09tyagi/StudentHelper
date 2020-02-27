package com.example.projectsetup.ui.chat

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.R
import com.example.projectsetup.data.models.ChatMessage
import com.example.projectsetup.util.Constants
import com.example.projectsetup.util.DateTimeUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cell_friend_chat.view.*

class FriendChatViewHolder(itemView:View, private val picasso: Picasso):RecyclerView.ViewHolder(itemView) {
    fun setChatMessage(chatMessage: ChatMessage) {
        chatMessage.access?.let {
            if (chatMessage.access == Constants.USER_ACCESS_ADMIN) {
                itemView.civFriend.setImageResource(R.mipmap.ic_launcher_round)
                itemView.clInnerFriendChat.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_admin_message)
                itemView.tvUserName.setTextColor(ContextCompat.getColor(itemView.context, R.color.off_white))
                itemView.messageTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                itemView.timeTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                itemView.tvUserName.text = Constants.USER_ACCESS_ADMIN
            } else {
                chatMessage.userAvatar?.let {
                    if (it.isNotBlank() && it.isNotEmpty()) {
                        picasso.load(it).placeholder(R.drawable.ic_avatar).error(R.drawable.ic_avatar).into(itemView.civFriend)
                    } else {
                        itemView.civFriend.setImageResource(R.drawable.ic_avatar)
                    }
                } ?: run {
                    itemView.civFriend.setImageResource(R.drawable.ic_avatar)
                }
                itemView.clInnerFriendChat.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_friend_chat_message)
                itemView.tvUserName.setTextColor(ContextCompat.getColor(itemView.context, R.color.color_text_secondary))
                itemView.timeTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.color_text_secondary))
                itemView.messageTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.color_text_primary))
                chatMessage.userName?.let {
                    itemView.tvUserName.text = it
                }
            }
            chatMessage.message?.let {
                itemView.messageTv.text = it
            }

            try {
                chatMessage.dataTime?.let {
                    itemView.timeTv.text = DateTimeUtils.getDateFromMillis(it as Long)
                }
            } catch (e: Exception) {
            }
        }
    }

}