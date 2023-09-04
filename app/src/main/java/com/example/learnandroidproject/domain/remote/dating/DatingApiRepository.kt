package com.example.learnandroidproject.domain.remote.dating

import com.example.learnandroidproject.common.GenericResult
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.GroupData
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.LoginRequest
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.UpdateUser
import com.example.learnandroidproject.data.local.model.dating.db.response.NewsBaseResponse
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.BaseGroupResponse
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import okhttp3.MultipartBody
import okhttp3.ResponseBody

interface DatingApiRepository {
    // as single form
    suspend fun fetchNews(): GenericResult<NewsBaseResponse>

    suspend fun fetchUsersForChatRooms(): GenericResult<List<UserInfo>>

    suspend fun fetchFriendsUsers(): GenericResult<List<UserInfo>>

    suspend fun fetchGroups(): GenericResult<List<GroupInfo>>

    suspend fun fetchUserData(): GenericResult<User>

    suspend fun fetchAllUsers(): GenericResult<List<UserInfo>>

    suspend fun getMessagesFromPage(id:String, page: Int, sendTime: Long): GenericResult<List<MessageItem>>

    suspend fun friendChatSendPhoto(id:String, image: MultipartBody.Part): GenericResult<ResponseBody>

    suspend fun getGroupMessagesFromPage(id: String, page: Int): GenericResult<BaseGroupResponse>

    suspend fun getUserProfile(id: String): GenericResult<User>

    suspend fun updateSeenInfoForUser(id: String): GenericResult<ResponseBody>

    suspend fun groupChatSendPhoto(id: String,image: MultipartBody.Part): GenericResult<ResponseBody>

    suspend fun updateSeenInfoForGroup(id: String): GenericResult<ResponseBody>

    suspend fun saveProfilePhoto(image: MultipartBody.Part): GenericResult<ResponseBody>

    suspend fun login(user: LoginRequest): GenericResult<ResponseBody>

    suspend fun updateProfile(user: UpdateUser): GenericResult<ResponseBody>

    suspend fun exit(): GenericResult<ResponseBody>

    suspend fun createGroup(group: GroupData): GenericResult<List<GroupInfo>>

    suspend fun register(user: User): GenericResult<ResponseBody>

}