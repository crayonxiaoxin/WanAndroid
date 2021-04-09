package com.github.crayonxiaoxin.wanandroid.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Repository {
    private val apiService = ApiService()

    suspend fun getHomeBanner3() = go { apiService.getHomeBanner() }

    private suspend fun <T> go(func: suspend CoroutineScope.() -> T): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                Result.Success(func())
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error(e)
            }
        }
    }
}