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
        viewModelScope.launch {
            bannerNetState.Loading()
            val res = Repository.getHomeBanner()
            if (res.succeeded) {
                bannerNetState.Success()
                bannerList.value = (res as Result.Success).data.data
            } else {
                bannerNetState.Error((res as Result.Error).exception.message)
            }
        }
    }

    var topArticleList = mutableStateOf(listOf<ArticleData>())
    val topArticleSize
        get() = topArticleList.value.size
    var topArticleNetState = mutableStateOf<NetState>(NetState.None)

    fun getTopArticles() {
        viewModelScope.launch {
            topArticleNetState.Loading()
            val res = Repository.getTopArticles()
            if (res.succeeded) {
                topArticleNetState.Success()
                topArticleList.value = (res as Result.Success).data.data
            } else {
                topArticleNetState.Error((res as Result.Error).exception.message)
            }
        }
    }

    var articleList = mutableStateOf(arrayListOf<ArticleData>())
    val articleListSize
        get() = articleList.value.size
    var articleNetState = mutableStateOf<NetState>(NetState.None)
    var articleCurrentPage = mutableStateOf(1)
    var articleTotalPage = mutableStateOf(1)
    fun getArticles() {
        if (articleCurrentPage.value <= articleTotalPage.value) {
            viewModelScope.launch {
                articleNetState.Loading()
                val res = Repository.getArticles(articleCurrentPage.value)
                if (res.succeeded) {
                    articleNetState.Success()
                    val data = (res as Result.Success).data.data
                    articleList.value.addAll(data.datas)
                    articleTotalPage.value = data.pageCount
                    articleCurrentPage.value += 1
                } else {
                    articleNetState.Error((res as Result.Error).exception.message)
                }
            }
        }

    }
}