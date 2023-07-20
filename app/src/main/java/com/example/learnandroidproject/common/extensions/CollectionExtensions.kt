package com.example.learnandroidproject.common.extensions

fun LongArray?.orEmpty(): LongArray {
    return this ?: longArrayOf()
}