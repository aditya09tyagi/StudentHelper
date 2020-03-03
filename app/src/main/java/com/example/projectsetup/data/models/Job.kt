package com.example.projectsetup.data.models

import com.google.gson.annotations.SerializedName

data class Job(
    @SerializedName("_v")
    val __v: Int,
    @SerializedName("_id")
    val id: String,
    @SerializedName("company")
    val companyId: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("description")
    val jobDescription: String,
    @SerializedName("place")
    val drivePlace: String,
    @SerializedName("skill")
    val commaSeparatedSkillIds: List<String>,
    @SerializedName("title")
    val jobTitle: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("visit_date")
    val visit_date: String
)