package com.example.learnandroidproject.domain.remote.dating

import com.example.learnandroidproject.common.GenericResult
import com.example.learnandroidproject.common.handleDatingRequest
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.GroupData
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.LoginRequest
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.UpdateUser
import com.example.learnandroidproject.data.local.model.dating.db.response.NewsBaseResponse
import com.example.learnandroidproject.data.remote.api.dating.DatingApiService
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.BaseGroupResponse
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import javax.inject.Inject

class DatingApiRepositoryImpl @Inject constructor(
    private val datingApiService: DatingApiService
) : DatingApiRepository {

    override suspend fun fetchNews(): GenericResult<NewsBaseResponse> = handleDatingRequest { datingApiService.news() }

    override suspend fun fetchUsersForChatRooms(): GenericResult<List<UserInfo>> = handleDatingRequest { datingApiService.fetchUsersForChatRooms() }

    override suspend fun fetchFriendsUsers(): GenericResult<List<UserInfo>> = handleDatingRequest { datingApiService.fetchFriendsUsers() }

    override suspend fun fetchGroups(): GenericResult<List<GroupInfo>> = handleDatingRequest { datingApiService.fetchGroups() }

    override suspend fun fetchUserData(): GenericResult<User> = handleDatingRequest { datingApiService.fetchUserData() }

    override suspend fun fetchAllUsers(): GenericResult<List<UserInfo>> = handleDatingRequest { datingApiService.fetchAllUsers() }

    override suspend fun getMessagesFromPage(id: String, page: Int): GenericResult<List<MessageItem>> = handleDatingRequest{ datingApiService.getMessagesFromPage(id,page)}

    override suspend fun getGroupMessagesFromPage(id: String, page: Int): GenericResult<BaseGroupResponse> = handleDatingRequest { datingApiService.getGroupMessagesFromPage(id,page) }

    override suspend fun getUserProfile(id: String): GenericResult<User> = handleDatingRequest { datingApiService.getUserProfile(id) }

    override suspend fun updateSeenInfoForUser(id: String): GenericResult<ResponseBody> = handleDatingRequest { datingApiService.updateSeenInfoForUser(id) }

    override suspend fun groupChatSendPhoto(id: String, image: MultipartBody.Part): GenericResult<ResponseBody> = handleDatingRequest { datingApiService.groupChatSendPhoto(id,image) }

    override suspend fun updateSeenInfoForGroup(id: String): GenericResult<ResponseBody> = handleDatingRequest { datingApiService.updateSeenInfoForGroup(id) }

    override suspend fun saveProfilePhoto(image: MultipartBody.Part): GenericResult<ResponseBody> = handleDatingRequest { datingApiService.saveProfilePhoto(image) }

    override suspend fun login(user: LoginRequest): GenericResult<ResponseBody> = handleDatingRequest { datingApiService.login(user) }

    override suspend fun exit(): GenericResult<ResponseBody> = handleDatingRequest { datingApiService.exit() }

    override suspend fun createGroup(group: GroupData): GenericResult<List<GroupInfo>> = handleDatingRequest { datingApiService.createGroup(group) }

    override suspend fun updateProfile(user: UpdateUser): GenericResult<ResponseBody> = handleDatingRequest { datingApiService.updateProfile(user) }

    override suspend fun register(user: User): GenericResult<ResponseBody> = handleDatingRequest { datingApiService.register(user) }
}