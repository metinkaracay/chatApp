package com.example.learnandroidproject.data.local.model.dating.db.response

import com.google.gson.annotations.SerializedName

data class NewsBaseResponse(
    @SerializedName("status") val status: String,
    @SerializedName("articles") val articles: List<NewsResponse>
)
