package com.example.projectsetup.ui.chat

import com.example.projectsetup.ui.base.BaseActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectsetup.R
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.di.components.DaggerChatActivityComponent
import com.example.projectsetup.ui.base.ItemDecorator
import com.example.projectsetup.util.Constants
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chat.*
import javax.inject.Inject

class ChatActivity : BaseActivity() {

    @Inject
    lateinit var adapter: ChatMultiTypeAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var databaseReference: FirebaseDatabase

    private var userName: String = ANONYMOUS
    private var userAccess: String = Constants.USER_ACCESS_NORMAL
    private var userAvatar: String? = null
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var userId: String
    private var projectId: String? = null
    private var currentListSize = 0


    companion object {
        private const val EXTRA_USER_ID = "EXTRA_USER_ID"
        private const val EXTRA_PROJECT_ID = "EXTRA_PROJECT_ID"
        private const val DATABASE_CHILD_MESSAGES = "messages"
        private const val ANONYMOUS = "anonymous"
        fun newIntent(context: Context, userId: String, projectId: String): Intent {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(EXTRA_USER_ID, userId)
            intent.putExtra(EXTRA_PROJECT_ID, projectId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        getArguments()
        inject()
        initialiseLayout()
        setListener()
        observeData()
    }

    private fun getArguments() {
        intent?.let {
            it.getStringExtra(EXTRA_USER_ID)?.let { uid ->
                userId = uid
            }
            it.getStringExtra(EXTRA_PROJECT_ID)?.let { pid ->
                projectId = pid
            }
        }
        userName = sharedPreferenceUtil.getString(Constants.EXTRA_USER_NAME)
    }

    private fun inject() {
        val component = DaggerChatActivityComponent.builder()
            .studentHelperApplicationComponent(
                StudentHelper.get(this).studentHelperApplicationComponent()
            )
            .build()

        component.injectChatActivityComponent(this)

        val referencePath = String.format("%s/%s", DATABASE_CHILD_MESSAGES, projectId)
        val databaseRef = databaseReference.getReference(referencePath)

        chatViewModel = ViewModelProvider(this, viewModelFactory).get(ChatViewModel::class.java)
        chatViewModel.init(databaseRef, adapter)

    }

    private fun initialiseLayout() {
        progressBarChat.visibility = View.VISIBLE
        setAdapter()
    }

    private fun setAdapter() {
        layoutManager = LinearLayoutManager(this)
        rvChat.layoutManager = layoutManager
        rvChat.adapter = adapter
        rvChat.addItemDecoration(ItemDecorator(30))
        if (::userId.isInitialized)
            adapter.setUserId(userId)
    }

    private fun setListener() {
        sendButton.setOnClickListener {
            sendMessageToCloud()
        }
    }

    private fun sendMessageToCloud() {
        if (messageInputEditText.text.trim().isNotEmpty() && ::userId.isInitialized) {
            val message = messageInputEditText.text.toString()

            if (userName.isEmpty() || userName.isBlank()) {
                userName = ANONYMOUS
            }

            chatViewModel.sendMessage(userId, userName, userAccess, userAvatar, message)
            messageInputEditText.setText("")
        }
    }

    private fun observeData() {
        chatViewModel.getLiveChat().observe(this, Observer {
            progressBarChat.visibility = View.GONE
            when (it.type) {
                Constants.TYPE_ADDED -> {
                    currentListSize++
                    chatViewModel.addChat(it)

                    if (it.userId == userId) {
                        rvChat.scrollToPosition(currentListSize - 1)
                    } else {
                        if (currentListSize - 2 == layoutManager.findLastCompletelyVisibleItemPosition())
                            rvChat.smoothScrollToPosition(currentListSize - 1)
                    }
                }
                Constants.TYPE_REMOVED -> {
                    currentListSize--
                    chatViewModel.removeChat(it)
                }
                Constants.TYPE_UPDATED -> {
                    chatViewModel.updateChat(it)
                }
            }

        })
    }
}