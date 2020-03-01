package com.example.projectsetup.data.models

import com.google.gson.annotations.SerializedName

data class Branch(
    @SerializedName("_v")
    val __v: Int,
    @SerializedName("_id")
    val id: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("name")
    val branchName: String,
    @SerializedName("subject")
    val commaSeparatedSubjectIds: List<String>,
    @SerializedName("updatedAt")
    val updatedAt: String
)