package com.example.projectsetup.data.models

import com.google.gson.annotations.SerializedName

data class Past(
    @SerializedName("_v")
    val __v: Int,
    @SerializedName("_id")
    val id: String,
    @SerializedName("company")
    val company: Company,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("description")
    val companyDescription: String,
    @SerializedName("isPlaced")
    val isPlaced: Boolean,
    @SerializedName("place")
    val driveLocation: String,
    @SerializedName("skill")
    val skills: List<Skill>,
    @SerializedName("title")
    val companyTitle: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("visit_date")
    val visitDate: String
)