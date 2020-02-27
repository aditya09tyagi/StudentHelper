package com.example.projectsetup.data.models

import com.example.projectsetup.util.Constants

data class ChatMessage(
    var key: String?,
    var dataTime: Any?,
    var message: String?,
    var userName: String?,
    var userId: String?,
    var userAvatar: String?,
    var access: String? = Constants.USER_ACCESS_NORMAL,
    var type: Int? = Constants.TYPE_ADDED
) {
    constructor() : this("", "", "", "", "", null, "")
}