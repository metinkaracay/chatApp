package com.example.learnandroidproject.data.remote.api.dating

import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.GroupData
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.LoginRequest
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.UpdateUser
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.data.local.model.dating.db.response.NewsBaseResponse
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.BaseGroupResponse
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.GroupInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*
import java.util.*

interface DatingApiService {

    /* Config Services */
    @GET("top-headlines?country=tr&apiKey=b13c7e532d894c6991d797f87385c876")
    suspend fun news(): NewsBaseResponse

    @GET("chats/mainPage")
    suspend fun fetchUsersForChatRooms(): List<UserInfo>
    @GET("chats/friends")
    suspend fun fetchFriendsUsers(): List<UserInfo>
    @GET("chats/groups")
    suspend fun fetchGroups(): List<GroupInfo>
    @GET("auth/profile")
    suspend fun fetchUserData(): User
    @GET("chats/allUsers")
    suspend fun fetchAllUsers(): List<UserInfo>
    @GET("chats/{id}")
    suspend fun getMessagesFromPage(@Path("id") id: String,
                                    @Query("page") page: Int ) : List<MessageItem>
    @GET("chats/group/{id}")
    suspend fun getGroupMessagesFromPage(@Path("id") id: String, @Query("page") page: Int) : BaseGroupResponse// List<MessageItem>
    @GET("auth/profile/{id}")
    suspend fun getUserProfile(@Path("id") id: String): User
    @GET("chats/{id}/seen")
    suspend fun updateSeenInfoForUser(@Path("id") id: String): ResponseBody
    @GET("chats/group/{id}/seen")
    suspend fun updateSeenInfoForGroup(@Path("id") id: String): ResponseBody
    @Multipart
    @POST("auth/profile/addPhoto")
    suspend fun saveProfilePhoto(@Part image: MultipartBody.Part): ResponseBody
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body user: LoginRequest): ResponseBody
    @PATCH("auth/profile/update")
    suspend fun updateProfile(@Body user: UpdateUser): ResponseBody
    @POST("auth/logout")
    suspend fun exit(): ResponseBody
    @POST("chats/createGroup")
    suspend fun createGroup(@Body group: GroupData): List<GroupInfo>//ResponseBody

    @Headers("Content-Type: application/json")
    @POST("auth/register")
    suspend fun register(@Body userFields: User): ResponseBody
}