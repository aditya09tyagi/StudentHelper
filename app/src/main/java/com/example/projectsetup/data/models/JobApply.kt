package com.example.projectsetup.data.models

import com.google.gson.annotations.SerializedName

data class JobApply(
    @SerializedName("_v")
    val __v: Int,
    @SerializedName("_id")
    val id: String,
    @SerializedName("job")
    val jobId: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("user")
    val userId: String
)