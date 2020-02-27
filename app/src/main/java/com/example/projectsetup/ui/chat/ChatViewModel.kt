package com.example.projectsetup.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectsetup.data.models.ChatMessage
import com.example.projectsetup.util.FirebaseQueryLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatViewModel @Inject constructor():ViewModel() {

    private lateinit var chatLiveData: LiveData<ChatMessage>
    private val chatList = ArrayList<ChatMessage>()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var chatAdapter: ChatMultiTypeAdapter


    fun init(databaseReference: DatabaseReference, adapter: ChatMultiTypeAdapter) {
        this.databaseReference = databaseReference
        this.chatAdapter = adapter
        this.chatAdapter.setItems(chatList)
    }

    fun getLiveChat(): LiveData<ChatMessage> {
        if (!::chatLiveData.isInitialized && ::databaseReference.isInitialized)
            chatLiveData = FirebaseQueryLiveData(databaseReference)
        return chatLiveData
    }

    fun addChat(chatMessage: ChatMessage) {
        if (::chatAdapter.isInitialized) {
            chatList.add(chatMessage)
            val currentSize = chatList.size
            chatAdapter.notifyItemInserted(currentSize - 1)
        }
    }

    fun removeChat(chatMessage: ChatMessage) {
        var position = -1
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                chatList.forEachIndexed { index, message ->
                    if (chatMessage.key == message.key) {
                        position = index
                        return@withContext
                    }
                }
            }
            if (position != -1) {
                chatList.removeAt(position)
                chatAdapter.notifyItemRemoved(position)
            }
        }
        /*function which will get executed in background thread
        It Will process the list and find the position of element
        Then we have to remove that element on that position and notify adapter
        */
    }

    fun updateChat(chatMessage: ChatMessage) {
        /*function which will get executed in background thread
        It Will process the list and find the position of element
        Then we have to update that element on that position and notify adapter
        */
        var position = -1
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                chatList.forEachIndexed { index, message ->
                    if (chatMessage.key == message.key) {
                        position = index
                        return@withContext
                    }
                }
            }
            if (position != -1) {
                chatList[position] = chatMessage
                chatAdapter.notifyItemChanged(position)
            }
        }
    }

    fun sendMessage(userId: String, userName: String, userAccess: String, userAvatar: String?, message: String) {
        if (::databaseReference.isInitialized) {
            databaseReference.push().key?.let { key ->
                val chatMessage = ChatMessage(key, ServerValue.TIMESTAMP, message, userName, userId, userAvatar, userAccess)
                databaseReference.child(key).setValue(chatMessage)
            }
        }
    }

}