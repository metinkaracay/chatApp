package com.example.learnandroidproject.data.local.model.dating.db.request.chatApp

import com.google.gson.annotations.SerializedName

class BaseRaceData(
   @SerializedName("Array") var raceDatas: List<RaceData>
) {
}