package com.example.projectsetup.di.modules.activity

import com.example.projectsetup.di.scopes.PerFragmentScope
import com.example.projectsetup.ui.chat.ChatMultiTypeAdapter
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides

@Module
class ChatActivityModule {

    @Provides
    @PerFragmentScope
    fun setChatAdapter(picasso:Picasso):ChatMultiTypeAdapter{
        return ChatMultiTypeAdapter(picasso=picasso)
    }

    @Provides
    @PerFragmentScope
    fun databaseReference(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }
}