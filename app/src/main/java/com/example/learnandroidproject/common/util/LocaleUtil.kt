package com.example.learnandroidproject.common.util

import androidx.core.text.isDigitsOnly
import java.util.*

class LocaleUtil {

    private val countries: List<Pair<String, String>> = Locale.getAvailableLocales()
        .map { Pair(it.displayCountry, it.country) }
        .filter { it.first.isNotBlank() && it.second.isDigitsOnly().not() }
        .distinct()

    fun findCountryByCode(code: String?): String? {
        val country = countries.firstOrNull { it.second.lowercase(Locale.getDefault()) == code?.lowercase(Locale.getDefault()) }
        return country?.first
    }
}