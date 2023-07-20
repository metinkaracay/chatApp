package com.example.learnandroidproject.common.extensions

fun Boolean?.orFalse(): Boolean = this ?: false

fun Boolean?.orTrue(): Boolean = this ?: true

fun Boolean.toInt(): Int = if (this) 1 else 0