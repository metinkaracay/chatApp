package com.example.learnandroidproject.ui.welcome.generalChatUsersFragment.adapter

import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo

data class GeneralUsersItemPageViewState(
    private val users: UserInfo
) {

    fun getUserName() = users.uName

    fun getUserStatus() = users.uStatu
}