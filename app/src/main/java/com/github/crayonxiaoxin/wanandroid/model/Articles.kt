package com.github.crayonxiaoxin.wanandroid.model

data class Articles(
    val `data`: ArticleList,
    val errorCode: Int,
    val errorMsg: String
)

data class ArticleList(
    val curPage: Int,
    val datas: List<ArticleData>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)