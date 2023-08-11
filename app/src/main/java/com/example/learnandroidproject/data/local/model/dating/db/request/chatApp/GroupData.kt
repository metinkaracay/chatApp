package com.example.learnandroidproject.data.local.model.dating.db.request.chatApp

import com.google.gson.annotations.SerializedName

data class GroupData(
    @SerializedName("groupName") var gName: String,
    @SerializedName("ids") var userIds: List<Int>
) {
}