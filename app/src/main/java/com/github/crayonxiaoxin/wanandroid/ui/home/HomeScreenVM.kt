package com.github.crayonxiaoxin.wanandroid.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.github.crayonxiaoxin.wanandroid.model.HomeBannerData

class HomeScreenVM : ViewModel() {
    var list = mutableStateOf(ArrayList<HomeBannerData>())
}