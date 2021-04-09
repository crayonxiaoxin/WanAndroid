package com.github.crayonxiaoxin.wanandroid.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.crayonxiaoxin.wanandroid.data.*
import com.github.crayonxiaoxin.wanandroid.model.ArticleData
import com.github.crayonxiaoxin.wanandroid.model.HomeBannerData
import kotlinx.coroutines.launch

class HomeScreenVM : ViewModel() {

    var bannerList = mutableStateOf(listOf<HomeBannerData>())
    val bannerSize
        get() = bannerList.value.size
    var bannerNetState = mutableStateOf<NetState>(NetState.None)

    fun getBanner() {
        bannerNetState.Loading()
        viewModelScope.launch {
            val res = Repository.getHomeBanner()
            if (res.succeeded) {
                bannerNetState.Success()
                bannerList.value = (res as Result.Success).data.data
            } else {
                bannerNetState.Error((res as Result.Error).exception)
            }
        }
    }

    var articleList = mutableStateOf(listOf<ArticleData>())
    val articleSize
        get() = articleList.value.size
    var articleNetState = mutableStateOf<NetState>(NetState.None)

    fun getTopArticles() {
        articleNetState.Loading()
        viewModelScope.launch {
            val res = Repository.getTopArticles()
            if (res.succeeded) {
                articleNetState.Success()
                articleList.value = (res as Result.Success).data.data
            } else {
                articleNetState.Error((res as Result.Error).exception)
            }
        }
    }
}