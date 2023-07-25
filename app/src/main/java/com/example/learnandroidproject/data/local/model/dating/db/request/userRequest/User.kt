package com.example.learnandroidproject.data.local.model.dating.db.request.userRequest

import com.google.gson.annotations.SerializedName

class User (
    @SerializedName("username") var userName: String?,
    @SerializedName("email") var email: String?,
    @SerializedName("password") var password: String?,
    @SerializedName("firstName") var firstName: String?,
    @SerializedName("lastName") var lastName: String?,
    @SerializedName("age") var age: String?,
    @SerializedName("gender") var gender: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("photoUrl") val photo: String?,
    ){

}