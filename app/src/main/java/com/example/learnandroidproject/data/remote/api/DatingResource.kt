package com.example.learnandroidproject.data.remote.api

import com.example.learnandroidproject.data.remote.model.dating.error.DatingApiException

data class DatingResource<out T>(val status: Status, val data: T?, val error: DatingApiException?) {
    companion object {
        fun <T> success(data: T?): DatingResource<T> {
            return DatingResource(Status.SUCCESS, data, null)
        }

        fun <T> error(datingApiException: DatingApiException): DatingResource<T> {
            return DatingResource(Status.ERROR, null, datingApiException)
        }

        fun <T> loading(): DatingResource<T> {
            return DatingResource(Status.LOADING, null, null)
        }
    }
}
