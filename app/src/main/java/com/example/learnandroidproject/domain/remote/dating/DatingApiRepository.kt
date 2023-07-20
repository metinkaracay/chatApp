package com.example.learnandroidproject.domain.remote.dating

import com.example.learnandroidproject.common.GenericResult
import com.example.learnandroidproject.data.local.model.dating.db.response.NewsBaseResponse
import com.example.learnandroidproject.data.remote.model.dating.response.postResponse.BookResponse
import com.example.learnandroidproject.data.remote.model.dating.response.testRespose.ImageResponse
import com.example.learnandroidproject.data.remote.model.dating.response.testRespose.PayloadBaseResponse
import com.example.learnandroidproject.data.remote.model.dating.response.testRespose.PayloadResponse
import com.example.learnandroidproject.data.remote.model.dating.response.testRespose.UserResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart

interface DatingApiRepository {
    // as single form
    suspend fun fetchNews(): GenericResult<NewsBaseResponse>

    suspend fun payLoad(): GenericResult<PayloadBaseResponse>

    suspend fun book(kitap1: BookResponse): GenericResult<BookResponse>

    suspend fun test(image: MultipartBody.Part): GenericResult<ResponseBody>

}