package com.github.crayonxiaoxin.wanandroid.data

sealed class NetState {
    object None : NetState()
    object Loading : NetState()
    object Success : NetState()
    object Refresh : NetState()
    data class Error(val errMsg: String?) : NetState()
}
