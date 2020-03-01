package com.example.projectsetup.data.models

import com.google.gson.annotations.SerializedName

data class Progress(
    @SerializedName("_v")
    val __v: Int,
    @SerializedName("_id")
    val _id: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deadline")
    val projectDeadline: String,
    @SerializedName("description")
    val projectDescription: String,
    @SerializedName("faculty")
    val facultyId: String,
    @SerializedName("members")
    val commaSeparatedMemberIds: List<String>,
    @SerializedName("progress")
    val progress: Int,
    @SerializedName("title")
    val projectTitle: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)