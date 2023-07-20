package com.example.learnandroidproject.common.extensions

import android.content.res.Resources
import kotlin.math.roundToInt

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

fun Int?.orZero(): Int {
    return this ?: 0
}

fun Int?.orValue(value: Int): Int {
    return this ?: value
}

fun Int?.toBoolean(): Boolean = this == 1

fun Long?.orZero(): Long {
    return this ?: 0
}

fun Int?.orMinusOne(): Int {
    return this ?: -1
}

fun Double?.orZero(): Double {
    return this ?: 0.0
}

fun Double?.orMinusOne(): Double {
    return this ?: -1.0
}

fun Double.isWhole(): Boolean {
    return this - this.toInt() == 0.0
}