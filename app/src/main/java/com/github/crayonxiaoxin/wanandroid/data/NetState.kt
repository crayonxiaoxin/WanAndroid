package com.github.crayonxiaoxin.wanandroid.data

import androidx.compose.runtime.MutableState
import java.lang.Exception

sealed class NetState {
    object None : NetState()
    object Loading : NetState()
    object Success : NetState()
    data class Error(val exception: Exception) : NetState()
}

fun MutableState<NetState>.Loading() {
    this.value = NetState.Loading
}

fun MutableState<NetState>.Success() {
    this.value = NetState.Success
}

fun MutableState<NetState>.None() {
    this.value = NetState.None
}

fun MutableState<NetState>.Error(exception: Exception) {
    this.value = NetState.Error(exception = exception)
}
