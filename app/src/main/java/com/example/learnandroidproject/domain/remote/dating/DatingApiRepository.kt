package com.example.learnandroidproject.domain.remote.dating

import com.example.learnandroidproject.common.GenericResult
import com.example.learnandroidproject.data.local.model.dating.db.response.NewsBaseResponse
import com.example.learnandroidproject.data.remote.model.dating.response.postResponse.BookResponse
import com.example.learnandroidproject.data.remote.model.dating.response.testRespose.*
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import okhttp3.MultipartBody
import okhttp3.ResponseBody

interface DatingApiRepository {
    // as single form
    suspend fun fetchNews(): GenericResult<NewsBaseResponse>

    suspend fun payLoad(): GenericResult<PayloadBaseResponse>

    suspend fun fetchUsersForChatRooms(): GenericResult<List<UserInfo>>

    suspend fun fetchUserData(): GenericResult<User>

    suspend fun saveProfilePhoto(image: MultipartBody.Part): GenericResult<ResponseBody>

    suspend fun book(kitap1: BookResponse): GenericResult<BookResponse>

    suspend fun test(image: MultipartBody.Part): GenericResult<ResponseBody>

    suspend fun test3(user: User): GenericResult<User>

    suspend fun register(user: User): GenericResult<ResponseBody>

}