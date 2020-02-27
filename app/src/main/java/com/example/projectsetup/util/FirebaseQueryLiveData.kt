package com.example.projectsetup.util

import androidx.lifecycle.LiveData
import com.example.projectsetup.data.models.ChatMessage
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class FirebaseQueryLiveData(private val databaseReference: DatabaseReference) : LiveData<ChatMessage>() {

    private val childEventListener = CustomChildEventListener()

    override fun onActive() {
        databaseReference.addChildEventListener(childEventListener)
    }

    override fun onInactive() {
        databaseReference.removeEventListener(childEventListener)
    }

    private inner class CustomChildEventListener : ChildEventListener {

        override fun onCancelled(dataSnapshot: DatabaseError) {

        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, p1: String?) {

        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
            dataSnapshot.getValue(ChatMessage::class.java)?.let { chat ->
                chat.type = Constants.TYPE_UPDATED
                value = chat
            }
        }

        override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
            dataSnapshot.getValue(ChatMessage::class.java)?.let { chat ->
                chat.type = Constants.TYPE_ADDED
                value = chat
            }
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            dataSnapshot.getValue(ChatMessage::class.java)?.let { chat ->
                chat.type = Constants.TYPE_REMOVED
                value = chat
            }
        }
    }
}