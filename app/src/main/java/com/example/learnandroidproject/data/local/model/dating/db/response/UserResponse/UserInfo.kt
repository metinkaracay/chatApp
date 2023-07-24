package com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("id") val uId: Int,
    @SerializedName("username") val uName: String,
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val uFirstName: String,
    @SerializedName("lastName") val uLastName: String,
    @SerializedName("age") val uAge: String,
    @SerializedName("gender") val uGender: String,
    @SerializedName("status") val uStatu: String
) {
}