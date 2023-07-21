package com.example.learnandroidproject.data.remote.model.dating.response.testRespose

import com.google.gson.annotations.SerializedName

data class TestResponse(
    @SerializedName("firstName") val firstName: String
) {
}