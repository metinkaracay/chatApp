package com.example.learnandroidproject.ui.welcome.testFragment

import com.example.learnandroidproject.data.remote.model.dating.response.testRespose.PayloadBaseResponse

class TestPageViewState(
    val payloadBaseResponse: PayloadBaseResponse
) {

    fun getId() = ""//payloadBaseResponse.payload.user.uId.toString()

    fun getEmail() = ""//payloadBaseResponse.payload.user.email
}