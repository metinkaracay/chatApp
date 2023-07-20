package com.example.learnandroidproject.common.extensions

import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned

fun String.toHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT);
    } else {
        Html.fromHtml(this);
    }
}

fun String.removeWhiteSpaces(): String {
    return this.replace("\\s".toRegex(), "")
}

fun String.lenEqOrGt(length: Int): Boolean = this.length >= length

fun String?.asUri(): Uri? {
    try {
        return Uri.parse(this)
    } catch (e: Exception) {
    }
    return null
}

fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.replaceFirstChar { f -> f.uppercase() } }