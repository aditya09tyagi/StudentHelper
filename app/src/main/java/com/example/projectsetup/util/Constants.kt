package com.example.projectsetup.util

interface Constants {

    companion object {

        //DeepLinks
        const val EXTRA_DEEP_LINK_SHARE_APP = 1
        const val SHARE_APP = "share/app"

        const val EXTRA_USER = "EXTRA_USER"
        const val EXTRA_USER_ID = "EXTRA_USER_ID"
        const val EXTRA_HEADER = "x-auth-token"

        //user access types
        const val USER_ACCESS_ADMIN = "ADMIN"
        const val USER_ACCESS_NORMAL = "User"

        //Chat Message State
        const val TYPE_ADDED = 0
        const val TYPE_REMOVED = 1
        const val TYPE_UPDATED = 2
    }
}

