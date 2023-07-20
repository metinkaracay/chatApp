package com.example.learnandroidproject.data.remote.api.dating

import android.content.Context
import android.content.Intent
import com.example.learnandroidproject.BaseUrlDecider
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.json.JSONObject
import javax.inject.Inject

class DatingApiAuthenticator @Inject constructor(
    private val context: Context
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val request = runBlocking {
            try {
                /*val tokenUrl = "${BaseUrlDecider.getApiBaseUrl()}auth/refreshToken"
                val refreshTokenResponse = Fuel.post(tokenUrl)
                    .header("Content-Type" to "application/json", HEADER_DATING_TOKEN to "Bearer")
                    .awaitString()
                val refreshTokenResponseObject = JSONObject(refreshTokenResponse)
                val newToken = refreshTokenResponseObject.getString("token")
                val refreshToken = refreshTokenResponseObject.getString("refreshToken")*/
                val newToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoyLCJlbWFpbCI6InRlc3QiLCJuYW1lIjoidGVzdFVzZXIiLCJjcmVhdGVkX2F0IjoiMjAyMy0wNy0xOVQwNjoyNjowOS4wMDBaIiwidXBkYXRlZF9hdCI6IjIwMjMtMDctMTlUMDY6MjY6MDkuMDAwWiJ9LCJpYXQiOjE2ODk3NTUzMjMsImV4cCI6MTY4OTg0MTcyM30.HSstmB4Jp1lq-Zz8uuJ-5q73urQqX5GXbJy0uexlJ3A"
                response.request.newBuilder()
                    .header(HEADER_DATING_TOKEN, "Bearer $newToken")
                    .build()
            } catch (exception: Exception) {
                null
            }
        }
        return if (request == null) {
            runBlocking {
                withContext(Dispatchers.Main) { launchAppAgain() }
            }
            null
        } else {
            request
        }
    }

    private fun launchAppAgain() {
        val packageManager = context.applicationContext.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.applicationContext.packageName)
        intent?.let {
            val componentName = it.component
            val mainIntent = Intent.makeRestartActivityTask(componentName)
            context.applicationContext.startActivity(mainIntent)
            Runtime.getRuntime().exit(0)
        }
    }

    companion object {
        private const val HEADER_DATING_TOKEN = "Authorization"
    }
}