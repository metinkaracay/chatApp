package com.example.learnandroidproject.data.remote.api

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

fun Status.isSuccess() = this == Status.SUCCESS
fun Status.isError() = this == Status.ERROR
fun Status.isLoading() = this == Status.LOADING
