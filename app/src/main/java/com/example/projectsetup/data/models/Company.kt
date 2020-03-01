package com.example.projectsetup.data.models

import com.google.gson.annotations.SerializedName

data class Company(
    @SerializedName("_v")
    val __v: Int,
    @SerializedName("_id")
    val id: String,
    @SerializedName("logo")
    val companyLogo: String,
    @SerializedName("name")
    val companyName: String,
    @SerializedName("website")
    val companyWebsite: String
)