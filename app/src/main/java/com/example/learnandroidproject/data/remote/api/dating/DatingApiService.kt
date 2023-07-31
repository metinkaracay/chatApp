package com.example.learnandroidproject.data.remote.api.dating

import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.LoginRequest
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.SendingMessage
import com.example.learnandroidproject.data.local.model.dating.db.request.chatApp.UpdateUser
import com.example.learnandroidproject.data.local.model.dating.db.request.userRequest.User
import com.example.learnandroidproject.data.local.model.dating.db.response.NewsBaseResponse
import com.example.learnandroidproject.data.local.model.dating.db.response.UserResponse.UserInfo
import com.example.learnandroidproject.data.local.model.dating.db.response.chatApp.MessageItem
import com.example.learnandroidproject.data.remote.model.dating.response.postResponse.BookResponse
import com.example.learnandroidproject.data.remote.model.dating.response.testRespose.PayloadBaseResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*
import java.util.*

interface DatingApiService {

    /* Config Services */
    @GET("top-headlines?country=tr&apiKey=b13c7e532d894c6991d797f87385c876")
    suspend fun news(): NewsBaseResponse

    @GET("auth/profile")
    suspend fun payLoad(): PayloadBaseResponse

    @GET("auth/mainPage")
    suspend fun fetchUsersForChatRooms(): List<UserInfo>
    @GET("auth/profile")
    suspend fun fetchUserData(): User
    @GET("chat/{id}")
    suspend fun getMessages(@Path("id") id: String): List<MessageItem>
    @Multipart
    @POST("auth/profile/addPhoto")
    suspend fun saveProfilePhoto(@Part image: MultipartBody.Part): ResponseBody
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body user: LoginRequest): ResponseBody
    @POST("chat/{id}")
    suspend fun sendMessage(@Path("id") id: String, @Body message: SendingMessage): ResponseBody
    @PATCH("auth/profile/update")
    suspend fun updateProfile(@Body user: UpdateUser): ResponseBody
    @POST("auth/logout")
    suspend fun exit(): ResponseBody

    @Headers("Content-Type: application/json")
    @POST("books")
    suspend fun books(@Body book: BookResponse): BookResponse

    @Multipart
    @POST("auth/test")
    suspend fun test(@Part image: MultipartBody.Part): ResponseBody

    @Headers("Content-Type: application/json")
    @POST("auth/register")
    suspend fun register(@Body userFields: User): ResponseBody
    @Headers("multipart/form-data")
    @POST("auth/register")
    suspend fun test3(@Body userFields: User): User
}