package com.example.learnandroidproject.common

import com.example.learnandroidproject.common.extensions.asDatingApiException
import com.example.learnandroidproject.data.remote.api.DatingResource
import com.example.learnandroidproject.data.remote.api.Resource
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import kotlin.time.Duration

suspend fun <T : Any> handleRequest(requestFunc: suspend () -> T): GenericResult<T> {
    return try {
        Ok(requestFunc.invoke())
    } catch (exception: Exception) {
        Err(exception)
    }
}

suspend fun <T : Any> handleDatingRequest(requestFunc: suspend () -> T): GenericResult<T> {
    return try {
        Ok(requestFunc.invoke())
    } catch (exception: Exception) {
        Err(exception.asDatingApiException())
    }
}

fun <T> handleRequestFlow(call: suspend () -> T): Flow<Resource<T>> {
    return flow {
        emit(Resource.loading())
        try {
            emit(Resource.success(call()))
        } catch (exception: Exception) {
            emit(Resource.error(exception))
        }
    }.flowOn(Dispatchers.IO)
}

fun <T> handleDatingRequestFlow(call: suspend () -> T): Flow<DatingResource<T>> {
    return flow {
        emit(DatingResource.loading())
        try {
            emit(DatingResource.success(call()))
        } catch (exception: Exception) {
            emit(DatingResource.error(exception.asDatingApiException()))
        }
    }.flowOn(Dispatchers.IO)
}


fun <T> handleDatabaseFlow(call: suspend () -> T): Flow<Resource<T>> {
    return flow {
        try {
            emit(Resource.success(call()))
        } catch (exception: Exception) {
            emit(Resource.error(exception))
        }
    }
}

fun <T1 : Any, T2 : Any, R : Any> multipleLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

fun <T1 : Any, T2 : Any, T3 : Any, R : Any> multipleLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}

fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any> multipleLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, block: (T1, T2, T3, T4) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}

fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, R : Any> multipleLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, p5: T5?, block: (T1, T2, T3, T4, T5) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null) block(p1, p2, p3, p4, p5) else null
}

inline fun <T> tryOrNull(block: () -> T): T? {
    return try {
        block()
    } catch (_: Exception) {
        null
    }
}

inline fun tryOrEmpty(block: () -> String): String {
    return try {
        block()
    } catch (_: Exception) {
        ""
    }
}

inline fun <T> tryOrLog(block: () -> T) {
    try {
        block()
    } catch (e: Exception) {
        e.let {
            Logger.e(e.message.orEmpty())
        }
    }
}

inline fun <reified T> castOrNull(from: Any?): T? = tryOrNull { from as? T }

fun tickerFlow(period: Duration, initialDelay: Duration = Duration.ZERO) = flow {
    delay(initialDelay)
    while (true) {
        emit(Unit)
        delay(period)
    }
}

fun tickFlow(millis: Long) = callbackFlow<Int> {
    val timer = Timer()
    var time = 0
    timer.scheduleAtFixedRate(
        object : TimerTask() {
            override fun run() {
                try {
                    trySend(time).isSuccess
                } catch (e: Exception) {}
                time += 1
            }
        },
        0,
        millis)
    awaitClose {
        timer.cancel()
    }
}