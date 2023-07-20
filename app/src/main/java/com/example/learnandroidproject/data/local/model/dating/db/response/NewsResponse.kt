package com.example.learnandroidproject.data.local.model.dating.db.response

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String
)
