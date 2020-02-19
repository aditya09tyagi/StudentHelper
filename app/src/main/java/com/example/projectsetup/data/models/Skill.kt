package com.example.projectsetup.data.models

import com.google.gson.annotations.SerializedName

data class Skill(
    @SerializedName("_v")
    val __v: Int,
    @SerializedName("_id")
    val id: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)