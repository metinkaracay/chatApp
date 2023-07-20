package com.example.learnandroidproject.data.remote.model.dating.response.postResponse

import com.google.gson.annotations.SerializedName

data class BookResponse(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("author") val author: List<String>
) {
}