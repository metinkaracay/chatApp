package com.example.learnandroidproject.common

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.getError

typealias GenericResult<T> = Result<T, Throwable>

fun <V, E> Result<V, E>.isSuccess(): Boolean = this.getError() == null

fun <V, E> Result<V, E>.isFailure(): Boolean = this.getError() != null