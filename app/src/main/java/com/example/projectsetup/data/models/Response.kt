package com.example.projectsetup.data.models

import com.google.gson.annotations.SerializedName

data class Response(
        @SerializedName("walletSysTransactionId")
        val walletSysTransactionId: String
)