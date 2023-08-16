package com.example.learnandroidproject.data.local.model.dating.db.response.chatApp

import com.google.gson.annotations.SerializedName

data class GroupMember(
    @SerializedName("id") var uId: Int,
    @SerializedName("username") var uName: String?,
    @SerializedName("status") var uStatu: String?,
    @SerializedName("photoUrl") var uPhoto: String?,
    @SerializedName("groupRole") var uRole: String?
) {
}