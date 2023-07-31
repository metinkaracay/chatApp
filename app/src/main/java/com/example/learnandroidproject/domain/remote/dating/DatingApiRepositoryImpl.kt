package com.example.learnandroidproject.domain.remote.dating

import com.example.learnandroidproject.common.GenericResult
import com.example.learnandroidproject.common.handleDatingRequest
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.LoginRequest
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.SendingMessage
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.UpdateUser
import com.example.learnandroidproject.data.local.model.dating.db.response.NewsBaseResponse
import com.example.learnandroidproject.data.remote.api.dating.DatingApiService
import com.example.learnandroidproject.data.remote.model.dating.response.postResponse.BookResponse
import com.example.learnandroidproject.data.remote.model.dating.response.testRespose.PayloadBaseResponse
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.Messages
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Path
import javax.inject.Inject

class DatingApiRepositoryImpl @Inject constructor(
    private val datingApiService: DatingApiService
) : DatingApiRepository {

    override suspend fun fetchNews(): GenericResult<NewsBaseResponse> = handleDatingRequest { datingApiService.news() }

    override suspend fun payLoad(): GenericResult<PayloadBaseResponse> = handleDatingRequest { datingApiService.payLoad() }

    override suspend fun fetchUsersForChatRooms(): GenericResult<List<UserInfo>> = handleDatingRequest { datingApiService.fetchUsersForChatRooms() }

    override suspend fun fetchUserData(): GenericResult<User> = handleDatingRequest { datingApiService.fetchUserData() }

    override suspend fun getMessages(id: String): GenericResult<List<MessageItem>> = handleDatingRequest { datingApiService.getMessages(id) }

    override suspend fun saveProfilePhoto(image: MultipartBody.Part): GenericResult<ResponseBody> = handleDatingRequest { datingApiService.saveProfilePhoto(image) }

    override suspend fun login(user: LoginRequest): GenericResult<ResponseBody> = handleDatingRequest { datingApiService.login(user) }

    override suspend fun sendMessage(id: String,message: SendingMessage) : GenericResult<ResponseBody> = handleDatingRequest { datingApiService.sendMessage(id, message) }

    override suspend fun exit(): GenericResult<ResponseBody> = handleDatingRequest { datingApiService.exit() }

    override suspend fun updateProfile(user: UpdateUser): GenericResult<ResponseBody> = handleDatingRequest { datingApiService.updateProfile(user) }
    override suspend fun book(kitap1: BookResponse): GenericResult<BookResponse> = handleDatingRequest { datingApiService.books(kitap1) }
    override suspend fun test(image: MultipartBody.Part): GenericResult<ResponseBody> = handleDatingRequest { datingApiService.test(image)}
    override suspend fun test3(user: User): GenericResult<User> = handleDatingRequest { datingApiService.test3(user)}

    override suspend fun register(user: User): GenericResult<ResponseBody> = handleDatingRequest { datingApiService.register(user) }
}