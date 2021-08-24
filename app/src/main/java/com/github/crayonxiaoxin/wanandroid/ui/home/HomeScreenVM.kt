package com.github.crayonxiaoxin.wanandroid.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.crayonxiaoxin.wanandroid.data.*
import com.github.crayonxiaoxin.wanandroid.model.ArticleData
import com.github.crayonxiaoxin.wanandroid.ui.common.BannerData
import com.github.crayonxiaoxin.wanandroid.ui.common.BaseVM
import com.github.crayonxiaoxin.wanandroid.ui.common.PageState
import kotlinx.coroutines.launch

class HomeScreenVM : BaseVM() {

    fun init(refresh: Boolean = false) {
        getBanner(refresh)
        getTopArticles()
        getArticles(refresh)
    }

    private val _bannerState = MutableLiveData<Result<List<BannerData>>>()
    val bannerState: LiveData<Result<List<BannerData>>> = _bannerState

    private fun getBanner(refresh: Boolean) {
        viewModelScope.launch {
            if (refresh) {
                loadState.value = LoadState.Refresh
            } else {
                pageState.value = PageState.Loading
                loadState.value = LoadState.Loading
            }
            val res = Repository.getHomeBanner()
            if (res.isOK) {
                if (refresh) {
                    loadState.value = LoadState.Success
                } else {
                    pageState.value = PageState.Content
                    loadState.value = LoadState.Success
                }
                _bannerState.value = Result.Success(res.data().data)
            } else {
                res.error().let {
                    _bannerState.value = Result.Error(it)
                    if (refresh) {
                        loadState.value = LoadState.Error(it.message)
                    } else {
                        pageState.value = PageState.Retry
                        loadState.value = LoadState.Error(it.message)
                    }
                }
            }
        }
    }

    var topArticleLoadState: LoadState = LoadState.None // 暂时用不到
    private val _topArticleState = MutableLiveData<Result<List<ArticleData>>>()
    var topArticleState: LiveData<Result<List<ArticleData>>> = _topArticleState

    private fun getTopArticles() {
        viewModelScope.launch {
            topArticleLoadState = LoadState.Loading
            val res = Repository.getTopArticles()
            if (res.isOK) {
                topArticleLoadState = LoadState.Success
                _topArticleState.value = Result.Success(res.data().data)
            } else {
                res.error().let {
                    topArticleLoadState = LoadState.Error(it.message)
                    _topArticleState.value = Result.Error(it)
                }
            }
        }
    }

    private val _articleState = MutableLiveData<Result<List<ArticleData>>>()
    var articleState: LiveData<Result<List<ArticleData>>> = _articleState
    var articleLoadState: LoadState = LoadState.None // 用于控制显示不同的 list footer
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
                    articleLoadState = LoadState.Loading
                    val res = Repository.getArticles(articleCurrentPage)
                    if (res.isOK) {
                        isLoading = false
                        articleLoadState = LoadState.Success
                        val data = res.data().data
                        if (_articleState.value != null && articleCurrentPage > 1) {
                            articleLastSize = _articleState.value?.data()?.size ?: 0
                            val newList: MutableList<ArticleData> = ArrayList()
                            newList.addAll(_articleState.value!!.data())
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
                        res.error().let {
                            articleLoadState = LoadState.Error(it.message)
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