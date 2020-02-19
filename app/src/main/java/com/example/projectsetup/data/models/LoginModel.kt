package com.example.projectsetup.data.models

import android.graphics.drawable.Drawable

data class LoginModel(
    var id:Int,
    var parentBackgroundResource:Drawable,
    var loginTypeText:String,
    var isSelected:Boolean = false
)