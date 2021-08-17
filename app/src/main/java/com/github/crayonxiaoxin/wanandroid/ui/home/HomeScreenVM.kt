package com.github.crayonxiaoxin.wanandroid.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.crayonxiaoxin.wanandroid.data.*
import com.github.crayonxiaoxin.wanandroid.model.ArticleData
import com.github.crayonxiaoxin.wanandroid.model.BannerData
import kotlinx.coroutines.launch

class HomeScreenVM : ViewModel() {

    fun init(refresh: Boolean = false) {
        getBanner(refresh)
        getTopArticles()
        getArticles(refresh)
    }

    //    var bannerNetState: NetState = NetState.None // 用于判断是否是初始化
    var bannerNetState = MutableLiveData<NetState>(NetState.None) // 用于判断是否是初始化
    private val _bannerState = MutableLiveData<Result<List<BannerData>>>()
    val bannerState: LiveData<Result<List<BannerData>>> = _bannerState

    private fun getBanner(refresh: Boolean) {
        viewModelScope.launch {
            bannerNetState.value = if (refresh) NetState.Refresh else NetState.Loading
            val res = Repository.getHomeBanner()
            if (res.succeeded) {
                bannerNetState.value = NetState.Success
                _bannerState.value = Result.Success(res.successData().data)
            } else {
                res.errorException().let {
                    _bannerState.value = Result.Error(it)
                    bannerNetState.value = NetState.Error(it.message)
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
                _topArticleState.value = Result.Success(res.successData().data)
            } else {
                res.errorException().let {
                    topArticleNetState = NetState.Error(it.message)
                    _topArticleState.value = Result.Error(it)
                }
            }
        }
    }

    private val _articleState = MutableLiveData<Result<List<ArticleData>>>()
    var articleState: LiveData<Result<List<ArticleData>>> = _articleState
    var articleNetState: NetState = NetState.None // 用于控制显示不同的 list footer
    var articleCurrentPage = 1
    var articleTotalPage = 1
    var articleLastSize = 0 // 當第一個可視item達到前一次的最後時，加載下一頁數據
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
                        val data = res.successData().data
                        if (_articleState.value != null && articleCurrentPage > 1) {
                            articleLastSize = _articleState.value?.successData()?.size ?: 0
                            val newList: MutableList<ArticleData> = ArrayList()
                            newList.addAll(_articleState.value!!.successData())
                            newList.addAll(data.datas)
                            _articleState.value = Result.Success(newList)
                        } else {
                            articleLastSize = if (data.datas.isEmpty()) 0 else 1
                            _articleState.value = Result.Success(data.datas)
                        }
                        articleTotalPage = data.pageCount
                        articleCurrentPage += 1
                    } else {
                        isLoading = false
                        res.errorException().let {
                            articleNetState = NetState.Error(it.message)
                            _articleState.value = Result.Error(it)
                        }
                    }
                }
            }
        } else {
            isLoading = false
        }
    }
}