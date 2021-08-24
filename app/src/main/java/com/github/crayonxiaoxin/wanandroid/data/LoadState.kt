package com.github.crayonxiaoxin.wanandroid.data

sealed class LoadState {
    object None : LoadState()
    object Loading : LoadState()
    object Success : LoadState()
    object Refresh : LoadState()
    data class Error(val errMsg: String?) : LoadState()
}
