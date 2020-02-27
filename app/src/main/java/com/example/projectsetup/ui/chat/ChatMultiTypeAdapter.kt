package com.example.projectsetup.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.R
import com.example.projectsetup.data.models.ChatMessage
import com.squareup.picasso.Picasso
import java.util.ArrayList

class ChatMultiTypeAdapter(private var userId: String? = null, private val picasso: Picasso):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var chatList: ArrayList<ChatMessage>

    companion object {
        private const val SELF_CHAT_VIEW = 0
        private const val FRIEND_CHAT_VIEW = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SELF_CHAT_VIEW) {
            SelfChatViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.cell_self_chat, parent, false)
                , picasso)
        } else {
            FriendChatViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.cell_friend_chat, parent, false)
                , picasso)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatList[position].userId == userId) SELF_CHAT_VIEW else FRIEND_CHAT_VIEW
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = chatList[position]
        when (holder.itemViewType) {
            SELF_CHAT_VIEW -> {
                (holder as SelfChatViewHolder).setChatMessage(chat)
            }
            else -> {
                (holder as FriendChatViewHolder).setChatMessage(chat)
            }
        }
    }

    fun setUserId(userId: String) {
        this.userId = userId
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    fun setItems(list: ArrayList<ChatMessage>) {
        chatList = list
    }
}