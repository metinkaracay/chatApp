package com.example.learnandroidproject.common.extensions

import java.util.*

val Date.age: Int
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = Date(time - Date().time)
        return 1970 - (calendar.get(Calendar.YEAR) + 1)
    }