package com.github.crayonxiaoxin.wanandroid.model

data class BannerModel(
    val `data`: List<BannerData>,
    val errorCode: Int,
    val errorMsg: String
)

data class BannerData(
    val id: Int,
    val title: String,
    val imagePath: String,
    val url: String,
    val desc: String?,
    val isVisible: Int?,
    val order: Int?,
    val type: Int?
)