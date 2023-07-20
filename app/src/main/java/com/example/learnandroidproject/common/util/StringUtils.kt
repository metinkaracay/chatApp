package com.example.learnandroidproject.common.util

object StringUtils {
    const val EMPTY = ""
    const val CHAMBER = "\"-_-\""
    const val PIPE = "|"
    const val COMMA = ","
    const val DOT = "."
    const val LINE = "\n"
    const val UNDERSCORE = "_"
    const val MINUS = "-"
    const val SPACE = " "
    const val TAB = "\t"
    const val PANDA = "-_-"
    const val PLUS = "+"
    const val EQUALS = "="
    const val SLASH = "/"
    const val AT = "@"
    const val STAR = "*"
    const val ZERO = "0"
    const val TWO_DOTS = ":"

    fun generateRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString(EMPTY)
    }
}