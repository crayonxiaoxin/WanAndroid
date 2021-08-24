package com.github.crayonxiaoxin.wanandroid.ui.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.crayonxiaoxin.wanandroid.data.LoadState

open class BaseVM:ViewModel() {
    var pageState = MutableLiveData<PageState>(PageState.Loading)
    var loadState = MutableLiveData<LoadState>(LoadState.None)
}