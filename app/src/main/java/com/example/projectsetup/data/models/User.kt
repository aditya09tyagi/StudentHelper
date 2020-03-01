package com.example.projectsetup.data.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_v")
    val __v: Int,
    @SerializedName("_id")
    val id: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("skills")
    val commaSeparatedSkillIds: List<String>,
    @SerializedName("subjects")
    val commaSeparatedSubjectIds: List<String>,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("branch")
    val branch: String,
    @SerializedName("section")
    val section: String,
    @SerializedName("user_type")
    val userType: Int,
    @SerializedName("age")
    val age: Int = 0,
    @SerializedName("semester")
    val semester: Int
)