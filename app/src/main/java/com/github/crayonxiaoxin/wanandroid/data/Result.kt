package com.github.crayonxiaoxin.wanandroid.data

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

val Result<*>.succeeded
    get() = this is Result.Success && data != null

fun <T> Result<*>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}
