package com.example.learnandroidproject.common

interface Mapper<T, R> {
    fun mapTo(from : T): R
}