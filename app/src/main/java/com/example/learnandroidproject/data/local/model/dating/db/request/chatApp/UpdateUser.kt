package com.example.learnandroidproject.data.local.model.dating.db.request.chatApp

import com.google.gson.annotations.SerializedName

class UpdateUser(
    @SerializedName("status") val uStatus: String?,
    @SerializedName("firstName") val uFirstName: String?,
    @SerializedName("lastName") val uLastName: String?,
    @SerializedName("age") val uAge: String?
) {
}