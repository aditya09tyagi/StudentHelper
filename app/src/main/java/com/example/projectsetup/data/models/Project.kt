package com.example.projectsetup.data.models

import com.google.gson.annotations.SerializedName

data class Project(
    @SerializedName("_v")
    val __v: Int,
    @SerializedName("_id")
    val _id: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("deadline")
    val deadline: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("faculty")
    val userFaculty: User,
    @SerializedName("members")
    val userMembers: List<User>,
    @SerializedName("progress")
    val progress: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)