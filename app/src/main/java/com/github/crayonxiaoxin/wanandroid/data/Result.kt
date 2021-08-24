package com.github.crayonxiaoxin.wanandroid.data

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

// Note: 第一步，判断请求是否成功
val Result<*>.isOK
    get() = this is Result.Success && data != null


// Note: 第二步，获取真实的数据
fun <T> Result<T>.data(): T {
    return (this as Result.Success).data
}

fun <T> Result<*>.dataOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}

// Note: 获取异常信息
fun Result<*>.error(): Exception {
    return (this as Result.Error).exception
}

// Note: 打印异常信息
fun Result<*>.errorPrint() {
    (this as Result.Error).exception.printStackTrace()
}
