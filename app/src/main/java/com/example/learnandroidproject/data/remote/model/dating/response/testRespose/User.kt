package com.example.learnandroidproject.data.remote.model.dating.response.testRespose

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("id") var id: Int,
    @SerializedName("username") var userName: String?,
    @SerializedName("email") var email: String?,
    //@SerializedName("password") var password: String?,
    //@SerializedName("firstName") var firstName: String?,
    //@SerializedName("lastName") var lastName: String?,
    //@SerializedName("age") var age: String?,
    //@SerializedName("gender") var gender: String?
) {
}