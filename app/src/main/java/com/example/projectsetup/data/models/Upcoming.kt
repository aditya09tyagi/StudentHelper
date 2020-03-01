package com.example.projectsetup.data.models

import com.google.gson.annotations.SerializedName

data class Upcoming(
    @SerializedName("_v")
    val __v: Int,
    @SerializedName("_id")
    val jobId: String,
    @SerializedName("company")
    val company: Company,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("isRegistered")
    val isRegistered: Boolean,
    @SerializedName("place")
    val place: String,
    @SerializedName("_skill")
    val skill: List<Skill>,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("visit_date")
    val visit_date: String
)