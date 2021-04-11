package com.github.crayonxiaoxin.wanandroid.data

import androidx.compose.runtime.MutableState
import java.lang.Exception

sealed class NetState {
    object None : NetState()
    object Loading : NetState()
    object Success : NetState()
    data class Error(val errMsg: String?) : NetState()
}
