package com.github.crayonxiaoxin.wanandroid.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.crayonxiaoxin.wanandroid.data.NetState
import com.github.crayonxiaoxin.wanandroid.data.Repository
import com.github.crayonxiaoxin.wanandroid.data.Result
import com.github.crayonxiaoxin.wanandroid.data.succeeded
import com.github.crayonxiaoxin.wanandroid.model.ArticleData
import com.github.crayonxiaoxin.wanandroid.model.BannerData
import kotlinx.coroutines.launch

class HomeScreenVM : ViewModel() {

    fun init(refresh: Boolean = false) {
        getBanner()
        getTopArticles()
        getArticles(refresh)
    }

    private val _bannerList = MutableLiveData<List<BannerData>>()
    val bannerList: LiveData<List<BannerData>> = _bannerList
    var bannerNetState: NetState = NetState.None

    fun getBanner() {
        viewModelScope.launch {
            bannerNetState = NetState.Loading
            val res = Repository.getHomeBanner()
            if (res.succeeded) {
                bannerNetState = NetState.Success
                _bannerList.value = (res as Result.Success).data.data
            } else {
                bannerNetState = NetState.Error((res as Result.Error).exception.message)
            }
        }
    }

    private val _topArticleList = MutableLiveData<List<ArticleData>>()
    var topArticleList = _topArticleList
    var topArticleNetState: NetState = NetState.None

    fun getTopArticles() {
        viewModelScope.launch {
            topArticleNetState = NetState.Loading
            val res = Repository.getTopArticles()
            if (res.succeeded) {
                topArticleNetState = NetState.Success
                _topArticleList.value = (res as Result.Success).data.data
            } else {
                topArticleNetState = NetState.Error((res as Result.Error).exception.message)
            }
        }
    }

    private val _articleList = MutableLiveData<List<ArticleData>>()
    var articleList = _articleList
    var articleNetState: NetState = NetState.None
    var articleCurrentPage = 1
    var articleTotalPage = 1
    var isLoading = false // 防止加载过快
    fun getArticles(isRefresh: Boolean = false) {
        if (isRefresh) articleCurrentPage = 1
        if (articleCurrentPage <= articleTotalPage) {
            viewModelScope.launch {
                if (!isLoading) {
                    isLoading = true
                    articleNetState = NetState.Loading
                    val res = Repository.getArticles(articleCurrentPage)
                    if (res.succeeded) {
                        isLoading = false
                        articleNetState = NetState.Success
                        val data = (res as Result.Success).data.data
                        if (_articleList.value != null && articleCurrentPage > 1) {
                            val newList: MutableList<ArticleData> = ArrayList()
                            newList.addAll(_articleList.value!!)
                            newList.addAll(data.datas)
                            _articleList.value = newList
                        } else {
                            _articleList.value = data.datas
                        }
                        articleTotalPage = data.pageCount
                        articleCurrentPage += 1
                    } else {
                        isLoading = false
                        articleNetState = NetState.Error((res as Result.Error).exception.message)
                    }
                }
            }
        } else {
            isLoading = false
        }
    }
}