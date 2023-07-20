package com.example.learnandroidproject.data.remote.model.dating.response.userResponse

import com.google.gson.annotations.SerializedName

class UserResponse (
    @SerializedName("username") val userName: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?,
    @SerializedName("firstName") val firstName: String?,
    @SerializedName("lastName") val lastName: String?,
    ){

}