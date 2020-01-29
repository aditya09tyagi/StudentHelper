package com.example.projectsetup.data.models

import com.google.gson.annotations.SerializedName

data class Error(

        @SerializedName("auth")
        val auth: Boolean = false,
        @SerializedName("message")
        val message: String = "Some Error Occurred"
)