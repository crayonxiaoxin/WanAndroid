package com.github.crayonxiaoxin.wanandroid.model

data class HomeBannerModel(
    val `data`: List<HomeBannerData>,
    val errorCode: Int,
    val errorMsg: String
)

data class HomeBannerData(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)