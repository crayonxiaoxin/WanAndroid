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
import com.github.crayonxiaoxin.wanandroid.model.BannerModel
import kotlinx.coroutines.launch

class HomeScreenVM : ViewModel() {

    fun init(refresh: Boolean = false) {
        getBanner()
        getTopArticles()
        getArticles(refresh)
    }

    var bannerNetState: NetState = NetState.None // 用于判断是否是初始化
    private val _bannerState = MutableLiveData<Result<List<BannerData>>>()
    val bannerState: LiveData<Result<List<BannerData>>> = _bannerState

    private fun getBanner() {
        viewModelScope.launch {
            bannerNetState = NetState.Loading
            val res = Repository.getHomeBanner()
            if (res.succeeded) {
                bannerNetState = NetState.Success
                (res as Result.Success).let {
                    _bannerState.value = Result.Success(it.data.data)
                }
            } else {
                (res as Result.Error).let {
                    _bannerState.value = Result.Error(it.exception)
                    bannerNetState = NetState.Error(it.exception.message)
                }
            }
        }
    }

    var topArticleNetState: NetState = NetState.None // 暂时用不到
    private val _topArticleState = MutableLiveData<Result<List<ArticleData>>>()
    var topArticleState: LiveData<Result<List<ArticleData>>> = _topArticleState

    private fun getTopArticles() {
        viewModelScope.launch {
            topArticleNetState = NetState.Loading
            val res = Repository.getTopArticles()
            if (res.succeeded) {
                topArticleNetState = NetState.Success
                (res as Result.Success).let {
                    _topArticleState.value = Result.Success(it.data.data)
                }
            } else {
                (res as Result.Error).let {
                    topArticleNetState = NetState.Error(it.exception.message)
                    _topArticleState.value = Result.Error(it.exception)
                }
            }
        }
    }

    private val _articleState = MutableLiveData<Result<List<ArticleData>>>()
    var articleState: LiveData<Result<List<ArticleData>>> = _articleState
    var articleNetState: NetState = NetState.None // 用于控制显示不同的 list footer
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
                        if (_articleState.value != null && articleCurrentPage > 1) {
                            val newList: MutableList<ArticleData> = ArrayList()
                            newList.addAll((_articleState.value!! as Result.Success).data)
                            newList.addAll(data.datas)
                            _articleState.value = Result.Success(newList)
                        } else {
                            _articleState.value = Result.Success(data.datas)
                        }
                        articleTotalPage = data.pageCount
                        articleCurrentPage += 1
                    } else {
                        isLoading = false
                        (res as Result.Error).let {
                            articleNetState = NetState.Error(it.exception.message)
                            _articleState.value = Result.Error(it.exception)
                        }
                    }
                }
            }
        } else {
            isLoading = false
        }
    }
}