package com.example.learnandroidproject.data.remote.api.dating

import com.example.learnandroidproject.data.local.model.dating.db.response.NewsBaseResponse
import com.example.learnandroidproject.data.remote.model.dating.response.postResponse.BookResponse
import com.example.learnandroidproject.data.remote.model.dating.response.testRespose.ImageResponse
import com.example.learnandroidproject.data.remote.model.dating.response.testRespose.PayloadBaseResponse
import com.example.learnandroidproject.data.remote.model.dating.response.testRespose.PayloadResponse
import com.example.learnandroidproject.data.remote.model.dating.response.testRespose.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*
import java.util.*

interface DatingApiService {

    /* Config Services */
    @GET("top-headlines?country=tr&apiKey=b13c7e532d894c6991d797f87385c876")
    suspend fun news(): NewsBaseResponse

    @GET("auth/profile")
    suspend fun payLoad(): PayloadBaseResponse

    @Headers("Content-Type: application/json")
    @POST("books")
    suspend fun books(@Body book: BookResponse): BookResponse

    @Multipart
    @POST("auth/test")
    suspend fun test(@Part image: MultipartBody.Part): ResponseBody
}